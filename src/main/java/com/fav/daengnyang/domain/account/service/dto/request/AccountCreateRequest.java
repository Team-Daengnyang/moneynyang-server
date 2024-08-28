package com.fav.daengnyang.domain.account.service.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountCreateRequest {
    private String accountTitle;
    private String accountImage;
    private String accountColor;
    private String accountNumber;
    private String accountTypeUniqueNo;
}