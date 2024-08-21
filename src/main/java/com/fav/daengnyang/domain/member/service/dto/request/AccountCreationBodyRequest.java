package com.fav.daengnyang.domain.member.service.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountCreationBodyRequest {
    private String accountTypeUniqueNo;

    @Builder
    public AccountCreationBodyRequest(String accountTypeUniqueNo) {
        this.accountTypeUniqueNo = accountTypeUniqueNo;
    }

    public static AccountCreationBodyRequest createAccountCreationBodyRequest(String accountTypeUniqueNo){
        return AccountCreationBodyRequest.builder()
                .accountTypeUniqueNo(accountTypeUniqueNo)
                .build();
    }
}
