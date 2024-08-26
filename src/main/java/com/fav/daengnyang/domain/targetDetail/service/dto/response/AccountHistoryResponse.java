package com.fav.daengnyang.domain.targetDetail.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountHistoryResponse {
    private String date;
    private String name;
    private int amount;
}