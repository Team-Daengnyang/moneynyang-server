package com.fav.daengnyang.domain.account.service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountInfoResponse {
    private String accountNumber;
    private String balance;
    private String bankName;
}
