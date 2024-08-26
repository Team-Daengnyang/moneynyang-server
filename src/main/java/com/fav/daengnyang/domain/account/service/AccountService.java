package com.fav.daengnyang.domain.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fav.daengnyang.domain.account.repository.AccountRepository;
import com.fav.daengnyang.domain.account.service.dto.request.AccountRequest;
import com.fav.daengnyang.domain.account.service.dto.response.AccountCreateResponse;
import com.fav.daengnyang.domain.account.service.dto.response.AccountResponse;
import com.fav.daengnyang.domain.targetDetail.service.dto.response.AccountHistoryResponse;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository bankbookRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.key}")
    private String apiKey;

    // 계좌 생성 메서드
    public AccountCreateResponse createBankbook(AccountRequest request, String userKey) throws JsonProcessingException {
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
        return AccountCreateResponse.builder()
                .bankbookNumber(savedBankbook.getBankbookNumber())
                .bankbookTitle(savedBankbook.getBankbookTitle())
                .bankbookImage(savedBankbook.getBankbookImage())
                .bankbookColor(savedBankbook.getBankbookColor())
                .build();
    }

    // 계좌 조회 메서드
    public AccountResponse inquireBankbook(Long memberId) throws JsonProcessingException {
        // 1. 계좌번호 가져오기
        Optional<Bankbook> optionalBankbook = bankbookRepository.findByMemberMemberId(memberId);

        if (!optionalBankbook.isPresent()) {
            throw new RuntimeException("해당 회원의 계좌를 찾을 수 없습니다.");
        }

        String bankbookNumber = optionalBankbook.get().getBankbookNumber();

        // 2. 외부 API를 통해 계좌 정보 조회
        Map<String, Object> responseMap = callInquireBankbookApi(bankbookNumber);

        return AccountResponse.builder()
                .bankbookTitle((String) responseMap.get("bankbookTitle"))
                .bankbookNumber((String) responseMap.get("bankbookNumber"))
                .bankbookImage((String) responseMap.get("bankbookImage"))
                .bankbookColor((String) responseMap.get("bankbookColor"))
                .build();
    }

    // 커스텀 색상 업데이트
    public AccountResponse updateBankbookColor(String bankbookNumber, String newColor) {
        Optional<Bankbook> optionalBankbook = bankbookRepository.findByBankbookNumber(bankbookNumber);

        if (!optionalBankbook.isPresent()) {
            throw new RuntimeException("해당 계좌 번호를 가진 계좌를 찾을 수 없습니다.");
        }

        Bankbook bankbook = optionalBankbook.get();
        bankbook.setBankbookColor(newColor);
        bankbookRepository.save(bankbook);

        return AccountResponse.builder()
                .bankbookTitle(bankbook.getBankbookTitle())
                .bankbookNumber(bankbook.getBankbookNumber())
                .bankbookImage(bankbook.getBankbookImage())
                .bankbookColor(bankbook.getBankbookColor())
                .build();
    }

    // 외부 금융 API 호출 (계좌 생성)
    private String callCreateBankbookApi(AccountRequest request, String userKey) throws JsonProcessingException {
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
        header.put("userKey", "userKey 받아와서 넣어야 하는데 내일 수정해봄");

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
    public List<AccountHistoryResponse> getBankbookHistory(String userKey) throws JsonProcessingException {
        // 외부 API 호출 후 데이터 처리
        Map<String, Object> historyData = callInquireTransactionHistoryApi(userKey);

        // API로부터 받은 데이터를 DTO로 매핑
        return mapToBankbookHistoryResponse(historyData);
    }

    // 금융 API 호출 (거래 내역 조회)
    private Map<String, Object> callInquireTransactionHistoryApi(String userKey) throws JsonProcessingException {
        // Header 및 Body 생성
        Map<String, Object> body = createTransactionHistoryRequest(userKey);

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 외부 API 호출
        String url = "/edu/demandDeposit/inquireTransactionHistory";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        // 응답 처리
        if (response.getStatusCode().is2xxSuccessful()) {
            return objectMapper.readValue(response.getBody(), Map.class);
        } else {
            throw new RuntimeException("API 호출 오류: " + response.getBody());
        }
    }

    // 거래 내역 조회 요청 생성
    private Map<String, Object> createTransactionHistoryRequest(String userKey) {
        return Map.of(
                "Header", createHeader("inquireTransactionHistory"),
                "userKey", userKey,
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

        return header;
    }

    private List<AccountHistoryResponse> mapToBankbookHistoryResponse(Map<String, Object> historyData) {
        // "history" 리스트를 가져와서 매핑
        List<Map<String, Object>> bankbookHistories = (List<Map<String, Object>>) historyData.get("history");

        return bankbookHistories.stream()
                .map(this::mapBankbookHistory)
                .collect(Collectors.toList());
    }

    private AccountHistoryResponse mapBankbookHistory(Map<String, Object> bankbookData) {
        // 데이터 매핑
        return AccountHistoryResponse.builder()
                .date((String) bankbookData.get("date"))
                .name((String) bankbookData.get("name"))
                .amount((Integer) bankbookData.get("amount"))
                .build();
    }
}
