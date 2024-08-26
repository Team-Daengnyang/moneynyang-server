package com.fav.daengnyang.domain.targetDetail.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fav.daengnyang.domain.targetDetail.repository.TargetDetailRepository;
import com.fav.daengnyang.domain.targetDetail.service.dto.response.TargetDetailSummaryResponse;
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

@Service
@RequiredArgsConstructor
public class TargetDetailService {

    private final TargetDetailRepository targetDetailRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.key}")
    private String apiKey;

    // Account 요약 정보 가져오기 메서드
    public TargetDetailSummaryResponse getTargetDetailSummary(Long accountId, String accountNo) throws JsonProcessingException {
        Integer totalAmount = targetDetailRepository.findTotalAmountByAccountId(accountId);
        Long transactionCount = targetDetailRepository.countTransactionsByAccountId(accountId);

        // 금융 API 호출 예시
        Map<String, Object> balanceResponse = callInquireAccountBalanceApi(accountNo);
        Map<String, Object> historyResponse = callInquireTransactionHistoryApi(accountNo);

        return TargetDetailSummaryResponse.builder()
                .totalAmount(totalAmount)
                .transactionCount(transactionCount)
                .apiBalance(balanceResponse.toString())
                .apiHistory(historyResponse.toString())
                .build();
    }

    // 금융 API 호출 (계좌 잔액 조회)
    private Map<String, Object> callInquireAccountBalanceApi(String accountNo) throws JsonProcessingException {
        // Header 및 Body 생성
        Map<String, Object> body = createBalanceRequest(accountNo);

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 외부 API 호출
        String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/inquireDemandDepositAccountBalance";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        // 응답 처리
        if (response.getStatusCode().is2xxSuccessful()) {
            return objectMapper.readValue(response.getBody(), Map.class);
        } else {
            throw new RuntimeException("API 호출 오류: " + response.getBody());
        }
    }

    // 금융 API 호출 (거래 내역 조회)
    private Map<String, Object> callInquireTransactionHistoryApi(String accountNo) throws JsonProcessingException {
        // Header 및 Body 생성
        Map<String, Object> body = createTransactionHistoryRequest(accountNo);

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 외부 API 호출
        String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/inquireTransactionHistory";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        // 응답 처리
        if (response.getStatusCode().is2xxSuccessful()) {
            return objectMapper.readValue(response.getBody(), Map.class);
        } else {
            throw new RuntimeException("API 호출 오류: " + response.getBody());
        }
    }

    // 잔액 조회 요청 생성
    private Map<String, Object> createBalanceRequest(String accountNo) {
        return Map.of(
                "Header", createHeader("inquireDemandDepositAccountBalance"),
                "accountNo", accountNo
        );
    }

    // 거래 내역 조회 요청 생성
    private Map<String, Object> createTransactionHistoryRequest(String accountNo) {
        return Map.of(
                "Header", createHeader("inquireTransactionHistory"),
                "accountNo", accountNo,
                "startDate", "20240101",
                "endDate", "20240331",
                "transactionType", "A",
                "orderByType", "ASC"
        );
    }

    // 공통 Header 생성
    private Map<String, String> createHeader(String apiName) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        Map<String, String> header = new HashMap<>();
        header.put("apiName", apiName);
        header.put("transmissionDate", now.format(dateFormatter));
        header.put("transmissionTime", now.format(timeFormatter));
        header.put("institutionCode", "00100");
        header.put("fintechAppNo", "001");
        header.put("apiKey", apiKey);
        header.put("userKey", "user_key_value");

        return header;
    }
}
