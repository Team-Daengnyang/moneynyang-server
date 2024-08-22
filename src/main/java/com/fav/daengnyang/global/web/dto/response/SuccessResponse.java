package com.fav.daengnyang.global.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<D> {
    private static final String DEFAULT_SUCCESS_MESSAGE = "요청을 성공했습니다.";

    private int status;
    private String message;
    private D data;

    public static <D> SuccessResponse<D> ok(D data){
        return new SuccessResponse<>(HttpStatus.OK, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public static SuccessResponse<?> ok(){
        return new SuccessResponse<>(HttpStatus.OK, DEFAULT_SUCCESS_MESSAGE);
    }

    public static <D> SuccessResponse<D> created(D data){
        return new SuccessResponse<>(HttpStatus.CREATED, DEFAULT_SUCCESS_MESSAGE);
    }

    public static SuccessResponse<?> update(){
        return new SuccessResponse<>(HttpStatus.OK, DEFAULT_SUCCESS_MESSAGE);
    }

    public static SuccessResponse<?> deleted(){
        return new SuccessResponse<>(HttpStatus.OK, DEFAULT_SUCCESS_MESSAGE);
    }

    @Builder
    public SuccessResponse(HttpStatus status, String message, D data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }

    @Builder
    public SuccessResponse(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }
}
