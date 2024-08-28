package com.fav.daengnyang.domain.account.service.dto.request;

import com.fav.daengnyang.global.web.dto.response.TransactionUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferHeaderRequest {
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
    private TransferHeaderRequest(String apiName, String transmissionDate, String transmissionTime,
                                  String institutionCode, String fintechAppNo, String institutionTransactionUniqueNo, String apiServiceCode,
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

    public static TransferHeaderRequest createTransferHeaderRequest(String apiKey, String userKey) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");


        return TransferHeaderRequest.builder()
                .apiName("updateDemandDepositAccountWithdrawal")
                .transmissionDate(now.format(dateFormatter))
                .transmissionTime(now.format(timeFormatter))
                .institutionCode("00100")
                .fintechAppNo("001")
                .apiServiceCode("updateDemandDepositAccountWithdrawal")
                .institutionTransactionUniqueNo(TransactionUtil.generateUniqueTransactionNo())
                .apiKey(apiKey)
                .userKey(userKey)
                .build();
    }
}