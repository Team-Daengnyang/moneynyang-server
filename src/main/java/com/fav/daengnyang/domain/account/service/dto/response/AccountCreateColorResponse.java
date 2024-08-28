package com.fav.daengnyang.domain.account.service.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class AccountCreateColorResponse {
    private String accountId;
    private String accountColor;
    private String accountImage;
}
