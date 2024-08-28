package com.fav.daengnyang.domain.targetDetail.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetailRequest {
    private String startDate;
    private String endDate;
    private String accountNo;
}
