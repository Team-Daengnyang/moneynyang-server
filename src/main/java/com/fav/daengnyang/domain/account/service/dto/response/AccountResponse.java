package com.fav.daengnyang.domain.account.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountResponse {
    private String accountTitle;
    private String accountNumber;
    private String accountColor;
}