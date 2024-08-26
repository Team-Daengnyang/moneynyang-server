package com.fav.daengnyang.domain.account.service.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BankbookRequest {
    private String bankbookTitle;
    private String bankbookImage;
    private String bankbookColor;
    private String accountTypeUniqueNo;
}