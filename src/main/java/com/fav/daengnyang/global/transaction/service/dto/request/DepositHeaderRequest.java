package com.fav.daengnyang.global.transaction.service.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepositHeaderRequest {
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
    private DepositHeaderRequest(String apiName, String transmissionDate, String transmissionTime, String institutionCode, String fintechAppNo, String apiServiceCode, String institutionTransactionUniqueNo, String apiKey, String userKey) {
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

    public static DepositHeaderRequest createDepositHeader(String apiKey, String userKey){
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String transmissionDate = today.format(formatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
        String transmissionTime = today.format(timeFormatter);

        return DepositHeaderRequest.builder()
                .apiName("updateDemandDepositAccountDeposit")
                .transmissionDate(transmissionDate)
                .transmissionTime(transmissionTime)
                .institutionCode("00100")
                .fintechAppNo("001")
                .apiServiceCode("updateDemandDepositAccountDeposit")
                .apiKey(apiKey)
                .userKey(userKey)
                .build();
    }

}
