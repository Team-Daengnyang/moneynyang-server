package com.fav.daengnyang.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USERNOTFOUND("존재하지 않는 유저입니다.", HttpStatus.UNAUTHORIZED),
    INVALIDPASSWORD("올바르지 않은 비밀번호입니다.", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
