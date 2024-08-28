package com.fav.daengnyang.domain.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fav.daengnyang.domain.account.entity.Account;
import com.fav.daengnyang.domain.account.entity.AccountCode;
import com.fav.daengnyang.domain.account.repository.AccountCodeRepository;
import com.fav.daengnyang.domain.account.repository.AccountRepository;
import com.fav.daengnyang.domain.account.service.dto.request.AccountRequest;
import com.fav.daengnyang.domain.account.service.dto.response.AccountCreateResponse;
import com.fav.daengnyang.domain.account.service.dto.response.AccountInfoResponse;
import com.fav.daengnyang.domain.account.service.dto.response.AccountResponse;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import com.fav.daengnyang.domain.targetDetail.service.dto.response.AccountHistoryResponse;
import com.fav.daengnyang.global.exception.CustomException;
import com.fav.daengnyang.global.exception.ErrorCode;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final AccountCodeRepository accountCodeRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.key}")
    private String apiKey;

    // 계좌 생성 메서드
    public AccountCreateResponse createAccount(AccountRequest request, String userKey, Long memberId) throws JsonProcessingException {
        // 외부 API를 통해 계좌를 생성
        String accountNo = callCreateAccountApi(request, userKey);

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // DB에 계좌 정보 저장
        Account account = Account.builder()
                .accountTitle(request.getAccountTitle())
                .accountNumber(accountNo)
                .accountColor(request.getAccountColor())
                .member(member)
                .build();

        Account savedAccount = accountRepository.save(account);

        // 응답 데이터 생성
        return AccountCreateResponse.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .accountTitle(savedAccount.getAccountTitle())
                .accountColor(savedAccount.getAccountColor())
                .build();
    }

    // 계좌 조회 메서드
    public AccountResponse inquireAccount(Long memberId, String userKey) throws JsonProcessingException {
        // 1. 계좌번호 가져오기
        Optional<Account> optionalAccount = accountRepository.findByMemberId(memberId);

        if (!optionalAccount.isPresent()) {
            throw new RuntimeException("해당 회원의 계좌를 찾을 수 없습니다.");
        }

        String accountNumber = optionalAccount.get().getAccountNumber();

        // 2. 외부 API를 통해 계좌 정보 조회
        Map<String, Object> responseMap = callInquireAccountApi(accountNumber, userKey);

        return AccountResponse.builder()
                .accountTitle((String) responseMap.get("accountTitle"))
                .accountNumber((String) responseMap.get("accountNumber"))
                .accountColor((String) responseMap.get("accountColor"))
                .build();
    }

    // 커스텀 색상 업데이트
    public AccountResponse updateAccountColor(Long memberId, String newColor) {

        Optional<Account> optionalAccount = accountRepository.findByMemberId(memberId);

        if (!optionalAccount.isPresent()) {
            throw new RuntimeException("해당 계좌 번호를 가진 계좌를 찾을 수 없습니다.");
        }

        Account account = optionalAccount.get();
        account.setAccountColor(newColor);
        accountRepository.save(account);

        return AccountResponse.builder()
                .accountTitle(account.getAccountTitle())
                .accountNumber(account.getAccountNumber())
                .accountColor(account.getAccountColor())
                .build();
    }

    // 외부 금융 API 호출 (계좌 생성)
    private String callCreateAccountApi(AccountRequest request, String userKey) throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        String accountTypeUniqueNo = "001-1-82fe5fa7eca441";

        // Header 생성
        Map<String, String> header = new HashMap<>();
        header.put("apiName", "createDemandDepositAccount");
        header.put("transmissionDate", now.format(dateFormatter));
        header.put("transmissionTime", now.format(timeFormatter));
        header.put("institutionCode", "00100");
        header.put("fintechAppNo", "001");
        header.put("apiServiceCode", "createDemandDepositAccount");
        header.put("institutionTransactionUniqueNo", TransactionUtil.generateUniqueTransactionNo());
        header.put("apiKey", apiKey);
        header.put("userKey", userKey);

        // Body 생성
        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("accountTitle", request.getAccountTitle());
        body.put("accountImage", request.getAccountImage());
        body.put("accountColor", request.getAccountColor());
        body.put("accountTypeUniqueNo", accountTypeUniqueNo);

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
    private Map<String, Object> callInquireAccountApi(String accountNo, String userKey) throws JsonProcessingException {
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
        header.put("institutionTransactionUniqueNo", TransactionUtil.generateUniqueTransactionNo());
        header.put("apiKey", apiKey);
        header.put("userKey", userKey);

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

    public List<AccountHistoryResponse> getAccountHistory(String userKey) throws JsonProcessingException {
        // 외부 API 호출 후 데이터 처리
        Map<String, Object> historyData = callInquireTransactionHistoryApi(userKey);

        // API로부터 받은 데이터를 DTO로 매핑
        return mapToAccountHistoryResponse(historyData);
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

    private List<AccountHistoryResponse> mapToAccountHistoryResponse(Map<String, Object> historyData) {
        // "history" 리스트를 가져와서 매핑
        List<Map<String, Object>> accountHistories = (List<Map<String, Object>>) historyData.get("history");

        return accountHistories.stream()
                .map(this::mapAccountHistory)
                .collect(Collectors.toList());
    }

    private AccountHistoryResponse mapAccountHistory(Map<String, Object> accountData) {
        // 데이터 매핑
        return AccountHistoryResponse.builder()
                .date((String) accountData.get("date"))
                .name((String) accountData.get("name"))
                .amount((Integer) accountData.get("amount"))
                .build();
    }

    // 계좌 정보 조회 메서드
    public AccountInfoResponse getAccountInfo(Long memberId, String userKey) throws JsonProcessingException {
        // 1. 계좌번호 가져오기
        Optional<Account> optionalAccount = accountRepository.findByMemberId(memberId);

        if (!optionalAccount.isPresent()) {
            throw new RuntimeException("해당 회원의 계좌를 찾을 수 없습니다.");
        }
        String accountTitle = optionalAccount.get().getAccountTitle();
        String accountNumber = optionalAccount.get().getAccountNumber();

        // 2. 외부 API를 통해 계좌 잔액 조회
        Map<String, Object> responseMap = callInquireAccountBalanceApi(accountNumber, userKey);

        int accountBalance = Integer.parseInt((String) responseMap.get("accountBalance"));
        String accountCode = (String) responseMap.get("accountCode");

        // 3. 계좌 코드와 매핑된 계좌 이름 가져오기
        Optional<AccountCode> optionalAccountCode = accountCodeRepository.findByAccountCode(accountCode);

        if (!optionalAccountCode.isPresent()) {
            throw new RuntimeException("해당 계좌 코드를 찾을 수 없습니다.");
        }

        String accountName = optionalAccountCode.get().getAccountName();

        // 4. 응답 데이터 생성
        return AccountInfoResponse.builder()
                .accountTitle(accountTitle)
                .accountNumber(accountNumber)
                .accountBalance(accountBalance)
                .bankName(accountName)
                .build();
    }

    // 외부 금융 API 호출 (계좌 잔액 조회)
    private Map<String, Object> callInquireAccountBalanceApi(String accountNo, String userKey) throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        // Header 생성
        Map<String, String> header = new HashMap<>();
        header.put("apiName", "inquireDemandDepositAccountBalance");
        header.put("transmissionDate", now.format(dateFormatter));
        header.put("transmissionTime", now.format(timeFormatter));
        header.put("institutionCode", "00100");
        header.put("fintechAppNo", "001");
        header.put("apiServiceCode", "inquireDemandDepositAccountBalance");
        header.put("institutionTransactionUniqueNo", TransactionUtil.generateUniqueTransactionNo());
        header.put("apiKey", apiKey);
        header.put("userKey", userKey);

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
        String url = "/edu/demandDeposit/inquireDemandDepositAccountBalance";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return objectMapper.readValue(response.getBody(), Map.class);
        } else {
            throw new RuntimeException("API 호출 오류: " + response.getBody());
        }
    }
}