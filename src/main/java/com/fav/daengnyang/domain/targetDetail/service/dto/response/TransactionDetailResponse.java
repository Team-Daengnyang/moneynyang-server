package com.fav.daengnyang.domain.targetDetail.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetailResponse {
    private Long transactionUniqueNo;
    private String transactionDate;
    private String transactionTime;
    private Long transactionBalance;
    private Long transactionAfterBalance;
    private String transactionSummary;
}
