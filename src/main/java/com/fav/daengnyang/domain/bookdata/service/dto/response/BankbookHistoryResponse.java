package com.fav.daengnyang.domain.bookdata.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankbookHistoryResponse {
    private String date;
    private String name;
    private int amount;
}