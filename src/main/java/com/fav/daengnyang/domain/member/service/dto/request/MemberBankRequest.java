package com.fav.daengnyang.domain.member.service.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberBankRequest {

    private String apiKey;
    private String userId;

    @Builder
    private MemberBankRequest(String apiKey, String userId) {
        this.apiKey = apiKey;
        this.userId = userId;
    }

    public static MemberBankRequest createMemberBankBodyRequest(String apiKey, String userId) {
        return MemberBankRequest.builder()
                .apiKey(apiKey)
                .userId(userId)
                .build();
    }
}
