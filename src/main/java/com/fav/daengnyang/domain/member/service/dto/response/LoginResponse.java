package com.fav.daengnyang.domain.member.service.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {
    private String accessToken;

    @Builder
    private LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static LoginResponse createLoginResponse(String accessToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
