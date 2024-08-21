package com.fav.daengnyang.domain.bankbook.service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class BankbookCreateResponse {
    private String accountNo;
    private String bankCode;
    private Map<String, String> currency;

}
