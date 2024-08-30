package com.fav.daengnyang.global.transaction.service;


import com.fav.daengnyang.global.transaction.service.dto.request.DepositHeaderRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final RestTemplate restTemplate;

    @Value("${api.key}")
    private String apiKey;

    // 입금하기
    public void makeDeposit(String accountNo, String userKey){
        // 1. body 객체 생성
        HashMap<String, Object> body = new HashMap<>();

        DepositHeaderRequest header = DepositHeaderRequest.createDepositHeader("updateDemandDepositAccountDeposit", apiKey, userKey);
        body.put("Header", header);
        body.put("accountNo", accountNo);
        body.put("transactionBalance", "1000000"); // 금액
        body.put("transactionSummary", "입금");

        // 2. HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. HttpEntity 객체 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 4. 외부 API 호출
        String url = "/edu/demandDeposit/updateDemandDepositAccountDeposit";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        log.info("입금하기 API 결과: " + response.getBody());
    }

    // 거래내역 메모하기
    public void makeMemo(String accountNo, String transactionNo, String memo, String userKey){
        // 1. body 객체 생성
        HashMap<String, Object> body = new HashMap<>();

        DepositHeaderRequest header = DepositHeaderRequest.createDepositHeader("transactionMemo", apiKey, userKey);
        body.put("Header", header);
        body.put("accountNo", accountNo);
        body.put("transactionUniqueNo", transactionNo); // 금액
        body.put("transactionMemo", memo);

        // 2. HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. HttpEntity 객체 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 4. 외부 API 호출
        String url = "/edu/transactionMemo";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        log.info("메모하기 API 결과: " + response.getBody());
    }
}
