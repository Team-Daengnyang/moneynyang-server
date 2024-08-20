package com.fav.daengnyang.domain.member.service.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateMemberBankResponseDto {

    private String userId;
    private String username;
    private String institutionCode;
    private String userKey;
    private String created;
    private String modified;

    @Builder
    private CreateMemberBankResponseDto(String userId, String username, String institutionCode, String userKey, String created, String modified){
        this.userId = userId;
        this.username = username;
        this.institutionCode = institutionCode;
        this.userKey = userKey;
        this.created = created;
        this.modified = modified;
    }

    public static CreateMemberBankResponseDto createResponseDto(String userId, String username, String institutionCode, String userKey, String created, String modified){
        return CreateMemberBankResponseDto.builder()
                .userId(userId)
                .username(username)
                .institutionCode(institutionCode)
                .userKey(userKey)
                .created(created)
                .modified(modified)
                .build();
    }
}
