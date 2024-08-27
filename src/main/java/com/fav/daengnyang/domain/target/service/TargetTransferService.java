package com.fav.daengnyang.domain.target.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import com.fav.daengnyang.domain.target.entity.Target;
import com.fav.daengnyang.domain.target.repository.TargetRepository;
import com.fav.daengnyang.domain.target.service.dto.request.TargetTransferRequest;
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

    // 계좌이체 금융 API
    void callTransferApi(String depositAccountNo, String withdrawalAccountNo, int amount, String userKey) throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        // Header 생성
        Map<String, String> header = new HashMap<>();
        header.put("apiName", "updateDemandDepositAccountTransfer");
        header.put("transmissionDate", now.format(dateFormatter));
        header.put("transmissionTime", now.format(timeFormatter));
        header.put("institutionCode", "00100");
        header.put("fintechAppNo", "001");
        header.put("apiServiceCode", "updateDemandDepositAccountTransfer");
        header.put("institutionTransactionUniqueNo", now.format(dateFormatter) + now.format(timeFormatter) + withdrawalAccountNo);
        header.put("apiKey", apiKey);
        header.put("userKey", userKey); // userKey 전달

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
