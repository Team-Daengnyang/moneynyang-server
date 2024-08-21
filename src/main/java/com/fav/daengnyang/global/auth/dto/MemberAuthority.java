package com.fav.daengnyang.global.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberAuthority {
    private String userKey;
    private String memberId;

    @Builder
    private MemberAuthority(String userKey, String memberId) {
        this.userKey = userKey;
        this.memberId = memberId;
    }

    public static MemberAuthority createMemberAuthority(String userKey, String memberId) {
        return builder().userKey(userKey).memberId(memberId).build();
    }
}
