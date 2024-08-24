package com.fav.daengnyang.domain.bankbook.service.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fav.daengnyang.domain.bankbook.entity.Bankbook;
import com.fav.daengnyang.domain.bankbook.repository.BankbookRepository;
import com.fav.daengnyang.domain.bankbook.service.dto.request.BankbookRequest;
import com.fav.daengnyang.domain.bankbook.service.dto.response.BankbookCreateResponse;
import com.fav.daengnyang.domain.bankbook.service.dto.response.BankbookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankbookService {

    private final BankbookRepository bankbookRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.key}")
    private String apiKey;

    // 계좌 생성 메서드
    public BankbookCreateResponse createBankbook(BankbookRequest request, String userKey) throws JsonProcessingException {
        // 외부 API를 통해 계좌를 생성
        String accountNo = callCreateBankbookApi(request, userKey);

        // DB에 계좌 정보 저장
        Bankbook bankbook = new Bankbook();
        bankbook.setBankbookTitle(request.getBankbookTitle());
        bankbook.setBankbookNumber(accountNo);
        bankbook.setBankbookImage(request.getBankbookImage());
        bankbook.setBankbookColor(request.getBankbookColor());

        Bankbook savedBankbook = bankbookRepository.save(bankbook);

        // 응답 데이터 생성
        return BankbookCreateResponse.builder()
                .bankbookNumber(savedBankbook.getBankbookNumber())
                .bankbookTitle(savedBankbook.getBankbookTitle())
                .bankbookImage(savedBankbook.getBankbookImage())
                .bankbookColor(savedBankbook.getBankbookColor())
                .build();
    }

    // 계좌 조회 메서드
    public BankbookResponse inquireBankbook(Long memberId) throws JsonProcessingException {
        // 1. 계좌번호 가져오기
        Optional<Bankbook> optionalBankbook = bankbookRepository.findByMemberMemberId(memberId);

        if (!optionalBankbook.isPresent()) {
            throw new RuntimeException("해당 회원의 계좌를 찾을 수 없습니다.");
        }

        String bankbookNumber = optionalBankbook.get().getBankbookNumber();

        // 2. 외부 API를 통해 계좌 정보 조회
        Map<String, Object> responseMap = callInquireBankbookApi(bankbookNumber);

        return BankbookResponse.builder()
                .bankbookTitle((String) responseMap.get("bankbookTitle"))
                .bankbookNumber((String) responseMap.get("bankbookNumber"))
                .bankbookImage((String) responseMap.get("bankbookImage"))
                .bankbookColor((String) responseMap.get("bankbookColor"))
                .build();
    }

    // 커스텀 색상 업데이트
    public BankbookResponse updateBankbookColor(String bankbookNumber, String newColor) {
        Optional<Bankbook> optionalBankbook = bankbookRepository.findByBankbookNumber(bankbookNumber);

        if (!optionalBankbook.isPresent()) {
            throw new RuntimeException("해당 계좌 번호를 가진 계좌를 찾을 수 없습니다.");
        }

        Bankbook bankbook = optionalBankbook.get();
        bankbook.setBankbookColor(newColor);
        bankbookRepository.save(bankbook);

        return BankbookResponse.builder()
                .bankbookTitle(bankbook.getBankbookTitle())
                .bankbookNumber(bankbook.getBankbookNumber())
                .bankbookImage(bankbook.getBankbookImage())
                .bankbookColor(bankbook.getBankbookColor())
                .build();
    }

    // 외부 금융 API 호출 (계좌 생성)
    private String callCreateBankbookApi(BankbookRequest request, String userKey) throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        // Header 생성
        Map<String, String> header = new HashMap<>();
        header.put("apiName", "createDemandDepositAccount");
        header.put("transmissionDate", now.format(dateFormatter));
        header.put("transmissionTime", now.format(timeFormatter));
        header.put("institutionCode", "00100");
        header.put("fintechAppNo", "001");
        header.put("apiServiceCode", "createDemandDepositAccount");
        header.put("institutionTransactionUniqueNo", now.format(dateFormatter) + now.format(timeFormatter) + userKey);
        header.put("apiKey", apiKey);
        header.put("userKey", userKey);

        // Body 생성
        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("bankbookTitle", request.getBankbookTitle());
        body.put("bankbookImage", request.getBankbookImage());
        body.put("bankbookColor", request.getBankbookColor());
        body.put("accountTypeUniqueNo", "001-1-ffa4253081d540");

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 객체 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 외부 API 호출
        String url = "/edu/demandDeposit/createDemandDepositAccount";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // 응답에서 accountNo 추출
            JsonNode responseBody = objectMapper.readTree(response.getBody());
            JsonNode recNode = responseBody.path("REC");
            return recNode.path("accountNo").asText();
        } else {
            throw new RuntimeException("API 호출 오류: " + response.getBody());
        }
    }

    // 외부 금융 API 호출 (계좌 조회)
    private Map<String, Object> callInquireBankbookApi(String accountNo) throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        // Header 생성
        Map<String, String> header = new HashMap<>();
        header.put("apiName", "inquireDemandDepositAccount");
        header.put("transmissionDate", now.format(dateFormatter));
        header.put("transmissionTime", now.format(timeFormatter));
        header.put("institutionCode", "00100");
        header.put("fintechAppNo", "001");
        header.put("apiServiceCode", "inquireDemandDepositAccount");
        header.put("apiKey", apiKey);
        header.put("userKey", "YOUR_USER_KEY_HERE");

        // Body 생성
        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("accountNo", accountNo);

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 객체 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 외부 API 호출
        String url = "/edu/demandDeposit/inquireDemandDepositAccount";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return objectMapper.readValue(response.getBody(), Map.class);
        } else {
            throw new RuntimeException("API 호출 오류: " + response.getBody());
        }
    }
}
