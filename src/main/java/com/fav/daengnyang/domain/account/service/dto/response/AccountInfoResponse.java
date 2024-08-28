package com.fav.daengnyang.domain.account.service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountInfoResponse {
    private String accountTitle;
    private String accountNumber;
    private int accountBalance;
    private String bankName;
}
