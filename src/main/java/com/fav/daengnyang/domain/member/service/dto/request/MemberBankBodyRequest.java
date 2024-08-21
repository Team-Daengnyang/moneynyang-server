package com.fav.daengnyang.domain.member.service.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberBankBodyRequest {

    private String apiKey;
    private String userId;

    @Builder
    private MemberBankBodyRequest(String apiKey, String userId) {
        this.apiKey = apiKey;
        this.userId = userId;
    }

    public static MemberBankBodyRequest createMemberBankBodyRequest(String apiKey, String userId) {
        return MemberBankBodyRequest.builder()
                .apiKey(apiKey)
                .userId(userId)
                .build();
    }
}
