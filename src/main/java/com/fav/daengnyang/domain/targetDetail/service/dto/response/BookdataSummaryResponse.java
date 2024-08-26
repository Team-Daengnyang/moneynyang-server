package com.fav.daengnyang.domain.targetDetail.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BookdataSummaryResponse {
    private Integer totalAmount;
    private Long transactionCount;
    private String apiBalance;
    private String apiHistory;
}
