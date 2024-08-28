package com.fav.daengnyang.domain.account.service.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountCreateColorRequest {
    private String accountColor;
    private String accountImage;
}
