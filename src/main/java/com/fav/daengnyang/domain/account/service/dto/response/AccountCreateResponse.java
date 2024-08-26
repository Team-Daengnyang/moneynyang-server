package com.fav.daengnyang.domain.account.service.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class AccountCreateResponse {
    private String accountNumber;
    private String accountTitle;
    private String accountImage;
    private String accountColor;
}