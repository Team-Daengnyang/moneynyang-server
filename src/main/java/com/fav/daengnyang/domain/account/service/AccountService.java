package com.fav.daengnyang.domain.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fav.daengnyang.domain.account.entity.Account;
import com.fav.daengnyang.domain.account.entity.AccountCode;
import com.fav.daengnyang.domain.account.repository.AccountCodeRepository;
import com.fav.daengnyang.domain.account.repository.AccountRepository;
import com.fav.daengnyang.domain.account.service.dto.request.AccountCreateColorRequest;
import com.fav.daengnyang.domain.account.service.dto.request.AccountCreateRequest;
import com.fav.daengnyang.domain.account.service.dto.request.TransferHeaderRequest;
import com.fav.daengnyang.domain.account.service.dto.request.TransferRequest;
import com.fav.daengnyang.domain.account.service.dto.response.AccountCreateColorResponse;
import com.fav.daengnyang.domain.account.service.dto.response.AccountCreateResponse;
import com.fav.daengnyang.domain.account.service.dto.response.AccountInfoResponse;
import com.fav.daengnyang.domain.account.service.dto.response.AccountResponse;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import com.fav.daengnyang.domain.pet.entity.Pet;
import com.fav.daengnyang.domain.pet.repository.PetRepository;
import com.fav.daengnyang.domain.targetDetail.service.dto.response.AccountHistoryResponse;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.aws.service.AwsService;
import com.fav.daengnyang.global.exception.CustomException;
import com.fav.daengnyang.global.exception.ErrorCode;
import com.fav.daengnyang.global.transaction.service.TransactionService;
import com.fav.daengnyang.global.web.dto.response.TransactionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final AccountCodeRepository accountCodeRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AwsService awsService;
    private final TransactionService transactionService;

    @Value("${api.key}")
    private String apiKey;

    private final List<String> names = Arrays.asList(
            "김민준", "이서준", "박지훈", "최성민", "정우진",
            "강현우", "조준호", "임태현", "오진우", "윤경민",
            "허시우", "송유진", "문재현", "장도현", "이기찬",
            "김남준", "김주원", "박원빈", "김한결", "오병호",
            "이승현", "김서연", "이지민", "박수빈", "최유진",
            "정예린", "강은지", "조수아", "임하늘", "오지아",
            "윤나연", "허민지", "송경희", "문진아", "장현정",
            "이소희", "김가연", "김유나", "박소연", "최세진",
            "김하은", "유서하"
    );
    private final Random random = new Random();
    private final List<String> categories = initializeCategories();

    public HashMap<String, String> getRandomName() {
        int index = random.nextInt(names.size());
        HashMap<String, String> data = new HashMap<>();
        data.put("name", names.get(index));
        return data;
    }


    private List<String> initializeCategories() {
        List<String> categoryList = new ArrayList<>();
        for (int i = 0; i < 3; i++) categoryList.add("식비");
        for (int i = 0; i < 2; i++) categoryList.add("교통");
        for (int i = 0; i < 2; i++) categoryList.add("쇼핑");
        for (int i = 0; i < 1; i++) categoryList.add("반려");
        categoryList.add("기타"); 
        return categoryList;
    }

    public AccountCreateResponse createAccount(AccountCreateRequest request, String userKey, Long memberId) throws JsonProcessingException {
        // 외부 API를 통해 계좌를 생성
        String accountNo = callCreateAccountApi(request, userKey);

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // DB에 계좌 정보 저장
        Account account = Account.builder()
                .accountTitle(request.getAccountTitle())
                .accountNumber(accountNo)
                .member(member)
                .build();

        Account savedAccount = accountRepository.save(account);

        // 응답 데이터 생성
        return AccountCreateResponse.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .accountTitle(savedAccount.getAccountTitle())
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
        Map<String, Object> recData = (Map<String, Object>) responseMap.get("REC");

        String bankCode = (String) recData.get("bankCode");

        // 3. 계좌 코드와 매핑된 계좌 이름 가져오기
        Optional<AccountCode> optionalAccountCode = accountCodeRepository.findByAccountCode(bankCode);

        if (!optionalAccountCode.isPresent()) {
            throw new RuntimeException("해당 계좌 코드를 찾을 수 없습니다.");
        }

        String accountName = optionalAccountCode.get().getAccountName();
        String accountColor = optionalAccount.get().getAccountColor();

        return AccountResponse.builder()
                .accountTitle(accountName)
                .accountNumber(accountNumber)
                .accountColor(accountColor)
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

    public String updateAccountImage(Long memberId, MultipartFile newImage) {

        Optional<Account> optionalAccount = accountRepository.findByMemberId(memberId);

        if (!optionalAccount.isPresent()) {
            throw new RuntimeException("해당 계좌 번호를 가진 계좌를 찾을 수 없습니다.");
        }
        
        Account account = optionalAccount.get();

        String accountImage = awsService.uploadFile(newImage, memberId);

        //url을 통해 S3에서 이미지 가져오기
        String url = awsService.getImageUrl(accountImage);

        account.setAccountColor(url);
        accountRepository.save(account);

        return url;
    }

    // 출금하기
    public void transferMoney(MemberPrincipal memberPrincipal, TransferRequest transferRequest) throws JsonProcessingException {
        //0. 계좌 번호
        Member member = memberRepository.findByMemberId(memberPrincipal.getMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 1. 금융 API 호출
        String transferNo = callTransferMoney(member.getDepositAccount(), memberPrincipal.getUserKey(), transferRequest);
        // 2. 메모하기
        log.info("transferNo: {}", transferNo);
        String memo = transferRequest.getName() + "," + setMemoCategory();
        transactionService.makeMemo(member.getDepositAccount(), transferNo, memo, memberPrincipal.getUserKey());
    }

    private String setMemoCategory(){
        int index = random.nextInt(categories.size());
        return categories.get(index);
    }

    private String callTransferMoney(String accountNo, String userKey, TransferRequest transferRequest) throws JsonProcessingException {
          //1. body 객체 생성
        TransferHeaderRequest header = TransferHeaderRequest
                .createTransferHeaderRequest(apiKey, userKey);
        HashMap<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("accountNo", accountNo);
        body.put("transactionBalance", transferRequest.getAmount());
        body.put("transactionSummary", "출금");

        // 2. HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. HttpEntity 객체 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 4. 외부 API 호출
        String url = "/edu/demandDeposit/updateDemandDepositAccountWithdrawal";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        // 5. 거래 번호 추출
        JsonNode rootNode = objectMapper.readTree((response.getBody()));
        JsonNode recNode = rootNode.path("REC");
        return recNode.path("transactionUniqueNo").asText();
    }

    // 외부 금융 API 호출 (계좌 생성)
    private String callCreateAccountApi(AccountCreateRequest request, String userKey) throws JsonProcessingException {
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
        header.put("institutionTransactionUniqueNo", TransactionUtil.generateUniqueTransactionNo());
        header.put("apiKey", apiKey);
        header.put("userKey", userKey);

        // Body 생성
        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("accountTitle", request.getAccountTitle());
        body.put("accountTypeUniqueNo", request.getAccountTypeUniqueNo());

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
        Optional<Member> optionalMember = memberRepository.findByMemberId(memberId);

        if (!optionalMember.isPresent()) {
            throw new RuntimeException("해당 회원의 계좌를 찾을 수 없습니다.");
        }
        String accountNumber = optionalMember.get().getDepositAccount();

        // 2. 외부 API를 통해 계좌 잔액 조회
        Map<String, Object> responseMap = callInquireAccountBalanceApi(accountNumber, userKey);
        Map<String, Object> recData = (Map<String, Object>) responseMap.get("REC");

        String accountBalance = recData.get("accountBalance").toString();
        String bankCode = (String) recData.get("bankCode");

        // 3. 계좌 코드와 매핑된 계좌 이름 가져오기
        Optional<AccountCode> optionalAccountCode = accountCodeRepository.findByAccountCode(bankCode);

        if (!optionalAccountCode.isPresent()) {
            throw new RuntimeException("해당 계좌 코드를 찾을 수 없습니다.");
        }

        String accountName = optionalAccountCode.get().getAccountName();

        // 4. 응답 데이터 생성
        return AccountInfoResponse.builder()
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

    public AccountCreateColorResponse createColorAccount(AccountCreateColorRequest request, Long memberId) {
        // 1. 회원과 연결된 계좌 찾기
        Account account = accountRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 2. account 커스텀
        account.updateAccount(request.getAccountColor(), request.getAccountImage());
        Account createdAccount = accountRepository.save(account);

        // 3. 응답 데이터 생성
        return AccountCreateColorResponse.builder()
                .accountId(createdAccount.getAccountNumber())
                .accountColor(createdAccount.getAccountColor())
                .accountImage(createdAccount.getAccountImage())
                .build();
    }
}