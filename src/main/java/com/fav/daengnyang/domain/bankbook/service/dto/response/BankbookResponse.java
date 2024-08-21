package com.fav.daengnyang.domain.bankbook.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BankbookResponse {
    private String accountTypeUniqueNo;
    private String userKey;
    private String customImageUrl;
    private String customColor;
}
