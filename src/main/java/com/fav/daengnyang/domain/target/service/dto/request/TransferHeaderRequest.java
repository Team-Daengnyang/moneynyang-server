package com.fav.daengnyang.domain.target.service.dto.request;

import com.fav.daengnyang.global.web.dto.response.TransactionUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Builder;

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

    // 헤더 생성 메소드
    public static TransferHeaderRequest createTransferHeaderRequest(String apiName, String apiServiceCode, String apiKey, String userKey) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        return TransferHeaderRequest.builder()
                .apiName(apiName)
                .transmissionDate(now.format(dateFormatter))
                .transmissionTime(now.format(timeFormatter))
                .institutionCode("00100")
                .fintechAppNo("001")
                .apiServiceCode(apiServiceCode)
                .institutionTransactionUniqueNo(TransactionUtil.generateUniqueTransactionNo())
                .apiKey(apiKey)
                .userKey(userKey)
                .build();
    }
}
