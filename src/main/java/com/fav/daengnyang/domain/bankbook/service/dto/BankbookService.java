package com.fav.daengnyang.domain.bankbook.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    public BankbookCreateResponse createBankbook(BankbookRequest request) throws JsonProcessingException {
        // 외부 API를 통해 userKey를 가져옴
        String bankbookNumber = callCreateBankbookApi(request);

        // DB에 계좌 정보 저장
        Bankbook bankbook = new Bankbook();
        bankbook.setBankbookTitle(request.getBankbookTitle());
        bankbook.setBankbookNumber(bankbookNumber);
        bankbook.setBankbookImage(request.getBankbookImage());
        bankbook.setBankbookColor(request.getBankbookColor());

        bankbookRepository.save(bankbook);

        // 응답 데이터 생성
        return BankbookCreateResponse.builder()
                .bankbookNumber(bankbook.getBankbookNumber())
                .bankbookTitle(bankbook.getBankbookTitle())
                .bankbookImage(bankbook.getBankbookImage())
                .bankbookColor(bankbook.getBankbookColor())
                .build();
    }

    // 계좌 조회 메서드
    public BankbookResponse inquireBankbook(String bankbookNumber) throws JsonProcessingException {
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
    private String callCreateBankbookApi(BankbookRequest request) throws JsonProcessingException {
        // 현재 날짜 및 시간 설정
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
        header.put("institutionTransactionUniqueNo", "20240215121212123562");
        header.put("apiKey", apiKey);

        // Body 생성
        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("bankbookTitle", request.getBankbookTitle());

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 객체 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 외부 API 호출
        String url = "/edu/demandDeposit/createDemandDepositAccount";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        // API 응답 처리
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);
            return (String) responseMap.get("bankbookNumber");
        } else {
            throw new RuntimeException("API 호출 오류: " + response.getBody());
        }
    }

    // 외부 금융 API 호출 (계좌 조회)
    private Map<String, Object> callInquireBankbookApi(String bankbookNumber) throws JsonProcessingException {
        // Header 및 Body 생성
        Map<String, Object> body = new HashMap<>();
        body.put("bankbookNumber", bankbookNumber);
        body.put("Header", createHeader());

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 외부 API 호출
        String url = "/edu/demandDeposit/inquireDemandDepositAccount";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        // 응답 처리
        if (response.getStatusCode().is2xxSuccessful()) {
            return objectMapper.readValue(response.getBody(), Map.class);
        } else {
            throw new RuntimeException("API 호출 오류: " + response.getBody());
        }
    }

    // Header 생성
    private Map<String, String> createHeader() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        Map<String, String> header = new HashMap<>();
        header.put("apiName", "inquireDemandDepositAccount");
        header.put("transmissionDate", now.format(dateFormatter));
        header.put("transmissionTime", now.format(timeFormatter));
        header.put("institutionCode", "00100");
        header.put("fintechAppNo", "001");
        header.put("apiServiceCode", "inquireDemandDepositAccount");
        header.put("institutionTransactionUniqueNo", "20240215121212123562");
        header.put("apiKey", apiKey);

        return header;
    }
}
