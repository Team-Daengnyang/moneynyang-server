package com.fav.daengnyang.domain.memberDetail.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransResponse {
    public String transactionType;
    public String transactionBalance;
    public String transactionSummary;
    public String transactionMemo;
    public String transactionDate;
}
