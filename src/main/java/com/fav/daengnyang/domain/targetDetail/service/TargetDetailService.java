package com.fav.daengnyang.domain.targetDetail.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fav.daengnyang.domain.targetDetail.service.dto.request.TransactionDetailRequest;
import com.fav.daengnyang.domain.targetDetail.service.dto.response.TransactionDetailResponse;
import com.fav.daengnyang.global.web.dto.response.TransactionUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TargetDetailService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.key}")
    private String apiKey;

    // 거래 내역 리스트 반환 메서드
    public List<TransactionDetailResponse> getTransactionDetails(String accountNo, String userKey, TransactionDetailRequest request) throws JsonProcessingException {
        List<TransactionDetailResponse> transactionDetails = new ArrayList<>();

        // 금융 API 호출 및 리스트 추출
        extractTransactionDetails(accountNo, userKey, request, transactionDetails);

        return transactionDetails;
    }

    // 금융 API 호출 및 거래 내역 리스트 추출
    private void extractTransactionDetails(String accountNo, String userKey, TransactionDetailRequest request, List<TransactionDetailResponse> transactionDetails) throws JsonProcessingException {
        // Header 및 Body 생성
        Map<String, Object> requestBody = createRequestBody(accountNo, userKey, request);

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // 외부 API 호출
        String url = "/edu/demandDeposit/inquireTransactionHistory";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        // 응답 처리
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);
            Map<String, Object> rec = (Map<String, Object>) responseMap.get("REC");
            List<Map<String, Object>> transactionList = (List<Map<String, Object>>) rec.get("list");

            // 각 거래 항목의 세부 정보를 리스트에 추가
            for (Map<String, Object> transaction : transactionList) {
                TransactionDetailResponse detail = TransactionDetailResponse.builder()
                        .transactionUniqueNo((String) transaction.get("transactionUniqueNo"))
                        .transactionDate((String) transaction.get("transactionDate"))
                        .transactionTime((String) transaction.get("transactionTime"))
                        .transactionBalance(Long.valueOf(transaction.get("transactionBalance").toString()))
                        .transactionAfterBalance(Long.valueOf(transaction.get("transactionAfterBalance").toString()))
                        .transactionSummary((String) transaction.get("transactionSummary"))
                        .build();
                transactionDetails.add(detail);
            }
        } else {
            throw new RuntimeException("API 호출 오류: " + response.getBody());
        }
    }

    // 공통 Header 및 Body 생성
    private Map<String, Object> createRequestBody(String accountNo, String userKey, TransactionDetailRequest request) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        Map<String, String> header = new HashMap<>();
        header.put("apiName", "inquireTransactionHistoryList");
        header.put("transmissionDate", now.format(dateFormatter));
        header.put("transmissionTime", now.format(timeFormatter));
        header.put("institutionCode", "00100");
        header.put("fintechAppNo", "001");
        header.put("apiServiceCode", "inquireTransactionHistoryList");
        header.put("institutionTransactionUniqueNo", TransactionUtil.generateUniqueTransactionNo());
        header.put("apiKey", apiKey);
        header.put("userKey", userKey);

        // Body 생성
        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("accountNo", accountNo);
        body.put("startDate", request.getStartDate());
        body.put("endDate", request.getEndDate());
        body.put("transactionType", "A");
        body.put("orderByType", "ASC");

        return body;
    }
}
