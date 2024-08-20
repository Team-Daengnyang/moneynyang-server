package com.fav.daengnyang.domain.member.service.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberBankResponse {

    private String userId;
    private String userName;
    private String institutionCode;
    private String userKey;
    private String created;
    private String modified;

    @Builder
    private MemberBankResponse(String userId, String userName, String institutionCode, String userKey, String created, String modified){
        this.userId = userId;
        this.userName = userName;
        this.institutionCode = institutionCode;
        this.userKey = userKey;
        this.created = created;
        this.modified = modified;
    }

    public static MemberBankResponse createResponse(String userId, String userName, String institutionCode, String userKey, String created, String modified){
        return MemberBankResponse.builder()
                .userId(userId)
                .userName(userName)
                .institutionCode(institutionCode)
                .userKey(userKey)
                .created(created)
                .modified(modified)
                .build();
    }
}
