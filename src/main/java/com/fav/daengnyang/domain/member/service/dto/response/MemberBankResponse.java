package com.fav.daengnyang.domain.member.service.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class MemberBankResponse {

    private String userId;
    private String userName;
    private String institutionCode;
    private String userKey;
    private String created;
    private String modified;

}
