package com.fav.daengnyang.domain.bankbook.service.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BankbookRequest {
    private String accountTypeUniqueNo;
    private String customImageUrl;
    private String customColor;
}
