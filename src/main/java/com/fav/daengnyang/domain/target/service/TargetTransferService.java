package com.fav.daengnyang.domain.target.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import com.fav.daengnyang.domain.target.entity.Target;
import com.fav.daengnyang.domain.target.repository.TargetRepository;
import com.fav.daengnyang.domain.target.service.dto.request.TargetTransferRequest;
import com.fav.daengnyang.domain.target.service.dto.request.TransferHeaderRequest;
import com.fav.daengnyang.domain.targetDetail.entity.TargetDetail;
import com.fav.daengnyang.domain.targetDetail.repository.TargetDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class TargetTransferService {

    private final TargetRepository targetRepository;
    private final TargetDetailRepository targetDetailRepository;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.key}")
    private String apiKey;

    // 목표에 입금하기
    public void transferToTarget(Long memberId, String userKey, Long targetId, TargetTransferRequest request) throws JsonProcessingException {
        // 유저와 타겟 정보 가져오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        Target target = targetRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 목표를 찾을 수 없습니다."));

        String depositAccountNo = target.getAccount().getAccountNumber(); // 입금 계좌
        String withdrawalAccountNo = member.getDepositAccount(); // 출금 계좌

        int amount = request.getAmount();

        // 금융 API 호출하여 이체 수행
        callTransferApi(depositAccountNo, withdrawalAccountNo, amount, userKey);

        // 타겟의 current_amount 업데이트 및 목표 달성 여부 확인
        int updatedAmount = target.getCurrentAmount() + amount;
        target.setCurrentAmount(updatedAmount);
        if (updatedAmount >= target.getTargetAmount()) {
            target.setIsDone(true);
            target.setEndDate(LocalDate.now());
        }
        targetRepository.save(target);

        // BankbookDetail에 새로운 이체 내역 추가
        TargetDetail detail = TargetDetail.builder()
                .amount(amount)
                .createdDate(LocalDate.now())
                .target(target)
                .build();
        targetDetailRepository.save(detail);
    }

    // 완료된 목표에서 출금하기 메소드
    @Transactional
    public void updateTarget(Long memberId, String userKey, Long targetId) throws JsonProcessingException {

        // Target 조회
        Target target = targetRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 목표를 찾을 수 없습니다."));

        // TargetDetail 리스트 조회 및 amount 합산
        List<TargetDetail> targetDetails = targetDetailRepository.findByTarget(target);
        int totalAmount = targetDetails.stream().mapToInt(TargetDetail::getAmount).sum();

        // TargetDetail 삭제
        targetDetailRepository.deleteAll(targetDetails);

        // Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));

        // 출금 계좌 (Target의 Bankbook)와 입금 계좌 (Member의 depositAccount) 정보 추출
        String withdrawalAccountNo = target.getAccount().getAccountNumber();
        String depositAccountNo = member.getDepositAccount();

        // 총 합계금액을 Member의 depositAccount로 이체
        if (totalAmount > 0) {
            callTransferApi(depositAccountNo, withdrawalAccountNo, totalAmount, userKey); // 재사용
        }

        // 출금 처리
        target.setIsWithdrawed(true);

        // 타겟의 현재 금액 0으로 설정
        target.setCurrentAmount(0);
    }

    // 목표 삭제 메소드
    @Transactional
    public void deleteTarget(Long memberId, Long targetId, String userKey) throws JsonProcessingException {

        // Target 조회
        Target target = targetRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 목표를 찾을 수 없습니다."));

        // TargetDetail 리스트 조회 및 amount 합산
        List<TargetDetail> targetDetails = targetDetailRepository.findByTarget(target);
        int totalAmount = targetDetails.stream().mapToInt(TargetDetail::getAmount).sum();

        // TargetDetail 삭제
        targetDetailRepository.deleteAll(targetDetails);

        // Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));

        // 출금 계좌 (Target의 Bankbook)와 입금 계좌 (Member의 depositAccount) 정보 추출
        String withdrawalAccountNo = target.getAccount().getAccountNumber();
        String depositAccountNo = member.getDepositAccount();

        // 총 합계금액을 Member의 depositAccount로 이체
        if (totalAmount > 0) {
            callTransferApi(depositAccountNo, withdrawalAccountNo, totalAmount, userKey); // 재사용
        }

        // Target 삭제
        targetRepository.delete(target);
    }

    // 계좌이체 금융 API
    void callTransferApi(String depositAccountNo, String withdrawalAccountNo, int amount, String userKey) throws JsonProcessingException {
        // Header 생성
        TransferHeaderRequest header = TransferHeaderRequest.createTransferHeaderRequest(
                "updateDemandDepositAccountTransfer", // apiName
                "updateDemandDepositAccountTransfer", // apiServiceCode
                apiKey,
                userKey);

        // Body 생성
        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("depositAccountNo", depositAccountNo);
        body.put("withdrawalAccountNo", withdrawalAccountNo);
        body.put("transactionBalance", amount);
        body.put("depositTransactionSummary", "(수시입출금) : 입금(이체)");
        body.put("withdrawalTransactionSummary", "(수시입출금) : 출금(이체)");

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 객체 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 외부 API 호출
        String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/updateDemandDepositAccountTransfer";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // 성공적으로 이체된 경우 처리
            JsonNode responseBody = objectMapper.readTree(response.getBody());
            String responseCode = responseBody.path("Header").path("responseCode").asText();
            if (!"H0000".equals(responseCode)) {
                throw new RuntimeException("이체 오류: " + responseBody.path("Header").path("responseMessage").asText());
            }
        } else {
            throw new RuntimeException("API 호출 오류: " + response.getBody());
        }
    }
}
