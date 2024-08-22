package com.fav.daengnyang.domain.member.service.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountCreationHeaderRequest {
    private String apiName;
    private String transmissionDate;
    private String transmissionTime;
    private String institutionCode;
    private String fintechAppNo;
    private String apiServiceCode;
    private String institutionTransactionUniqueNo;
    private String apiKey;
    private String userKey;

    @Builder
    private AccountCreationHeaderRequest(String apiName, String transmissionDate, String transmissionTime,
                                         String institutionCode, String fintechAppNo,String institutionTransactionUniqueNo, String apiServiceCode,
                                         String apiKey, String userKey) {
        this.apiName = apiName;
        this.transmissionDate = transmissionDate;
        this.transmissionTime = transmissionTime;
        this.institutionCode = institutionCode;
        this.fintechAppNo = fintechAppNo;
        this.apiServiceCode = apiServiceCode;
        this.institutionTransactionUniqueNo = institutionTransactionUniqueNo;
        this.apiKey = apiKey;
        this.userKey = userKey;
    }

    public static AccountCreationHeaderRequest createAccountCreationHeaderRequest(String apiKey, String userKey) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");


        return AccountCreationHeaderRequest.builder()
                .apiName("createDemandDepositAccount")
                .transmissionDate(now.format(dateFormatter))
                .transmissionTime(now.format(timeFormatter))
                .institutionCode("00100")
                .fintechAppNo("001")
                .apiServiceCode("createDemandDepositAccount")
                .institutionTransactionUniqueNo(generateUniqueTransactionNo(now))
                .apiKey(apiKey)
                .userKey(userKey)
                .build();
    }

    private static String generateUniqueTransactionNo(LocalDateTime dateTime) {
        String dateTimePrefix = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomSuffix = String.format("%06d", new Random().nextInt(1000000)); // 6자리 난수 생성
        return dateTimePrefix + randomSuffix;
    }
}
