package com.fav.daengnyang.domain.memberDetail.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class InquireTransactionResponse {
    @JsonProperty("REC")
    private RecResponse rec;
}

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
class RecResponse{
    @JsonProperty("totalCount")
    private String totalCount;
    private List<TransactionResponse> list;
}

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
class TransactionResponse{
    private String transactionType;
    private String transactionBalance;
    private String transactionSummary;
    private String transactionMemo;
    private String transactionDate;
}