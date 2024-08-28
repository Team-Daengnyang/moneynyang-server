package com.fav.daengnyang.domain.member.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponse {
    private Long memberId;
    private String memberName;
    private int memberLevel;
    private long memberDate;
    private int memberTarget;
}
