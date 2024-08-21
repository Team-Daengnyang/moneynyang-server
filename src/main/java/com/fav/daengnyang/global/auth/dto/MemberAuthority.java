package com.fav.daengnyang.global.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberAuthority {
    private String userKey;
    private String userId;

    @Builder
    private MemberAuthority(String userKey, String userId) {
        this.userKey = userKey;
        this.userId = userId;
    }

    public static MemberAuthority createMemberAuthority(String userKey, String userId) {
        return builder().userKey(userKey).userId(userId).build();
    }
}
