package com.fav.daengnyang.domain.memberDetail.service.dto.request;

import com.fav.daengnyang.domain.member.service.dto.request.AccountCreationHeaderRequest;
import com.fav.daengnyang.global.web.dto.response.TransactionUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquireTransactionHeader {
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
    private InquireTransactionHeader(String apiName, String transmissionDate, String transmissionTime,
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

    public static InquireTransactionHeader createInquireTransactionHeader(String apiKey, String userKey) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");


        return InquireTransactionHeader.builder()
                .apiName("inquireTransactionHistoryList")
                .transmissionDate(now.format(dateFormatter))
                .transmissionTime(now.format(timeFormatter))
                .institutionCode("00100")
                .fintechAppNo("001")
                .apiServiceCode("inquireTransactionHistoryList")
                .institutionTransactionUniqueNo(TransactionUtil.generateUniqueTransactionNo())
                .apiKey(apiKey)
                .userKey(userKey)
                .build();
    }
}
