package com.fav.daengnyang.domain.member.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountCreationHeaderRequest {

    // api 이름
    private String apiName = "createDemandDepositAccount";
    // api 전송일자
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate transmissionDate;
    // api 전송 시각
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HHmmss")
    private LocalDateTime transmissionTime;
    // 기관코드 - 항상 고정
    private String institutionCode = "00100";
    // 핀테크 앱 일련번호 - 항상 고정
    private String fintechAppNo = "001";
    // 상품 코드
    private String apiServiceCode;
    // 기관거래 고유번호
    private String institutionTransactionUniqueNo;
    // 앱 관리자 key
    private String apiKey;
    // 앱 사용자 key
    private String userKey;

    @Builder
    private AccountCreationHeaderRequest(String apiServiceCode, String institutionTransactionUniqueNo, String apiKey, String userKey){
        this.apiServiceCode = apiServiceCode;
        this.institutionTransactionUniqueNo = institutionTransactionUniqueNo;
        this.apiKey = apiKey;
        this.userKey = userKey;

        this.transmissionDate = LocalDate.now();
        this.transmissionTime = LocalDateTime.now();
    }

    public static AccountCreationHeaderRequest createAccountCreationHeaderRequest(String apiServiceCode, String institutionTransactionUniqueNo, String apiKey, String userKey){
        return AccountCreationHeaderRequest.builder()
                .apiServiceCode(apiServiceCode)
                .institutionTransactionUniqueNo(institutionTransactionUniqueNo)
                .apiKey(apiKey)
                .userKey(userKey)
                .build();
    }
}
