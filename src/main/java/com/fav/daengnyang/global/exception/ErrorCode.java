package com.fav.daengnyang.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("존재하지 않는 유저입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_PASSWORD("올바르지 않은 비밀번호입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("올바르지 않은 ACCESSTOKEN 입니다", HttpStatus.UNAUTHORIZED),
    JSON_PROCESSING_ERROR("JSON 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
