package com.fav.daengnyang.domain.member.service.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreatedRequest {
    private String email;
    private String password;
    private String name;
}
