package com.fav.daengnyang.global.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

import com.fav.daengnyang.global.dto.response.ExceptionResponse;
import com.fav.daengnyang.global.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(CustomException ex) {
        ExceptionResponse response = buildResponse(ex.getErrorCode().getHttpStatus(), ex);
        return new ResponseEntity<>(response, ex.getErrorCode().getHttpStatus());
    }


    // 클라이언트의 잘못된 요청으로 발생하는 예외 처리
    @ExceptionHandler({
            IllegalArgumentException.class,         // 잘못된 인수로 인해 발생
            ServletRequestBindingException.class,   // HTTP 요청 바인딩 문제
            HttpMessageNotReadableException.class,  // 읽을 수 없는 HTTP 메시지
            MissingServletRequestPartException.class,   // 요청 일부가 누락된 경우
            MissingServletRequestParameterException.class,  // 요청 파라미터 누락
            TypeMismatchException.class,            // 요청 파라미터 타입 불일치
            BindException.class,                    // 바인딩 오류
    })
    public ResponseEntity<ExceptionResponse> badRequestHandler(Exception exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(buildResponse(BAD_REQUEST, exception));
    }

    // 존재하지 않는 URL 또는 리소스를 요청할 때 발생하는 예외를 처리
    @ExceptionHandler({
            NoHandlerFoundException.class,      // 요청에 대한 핸들러가 없을 때 발생
            NoResourceFoundException.class      // 요청된 리소스를 찾을 수 없을 때 발생
    })
    public ResponseEntity<ExceptionResponse> notFoundExceptionHandler(Exception exception) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(buildResponse(NOT_FOUND, exception));
    }

    // 지원되지 않는 HTTP 메서드 요청 시 발생하는 예외 처리
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> methodNotAllowedExceptionHandler(
            HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity
                .status(METHOD_NOT_ALLOWED)
                .body(buildResponse(METHOD_NOT_ALLOWED, exception));
    }

    // 클라이언트가 요청한 미디어 타입을 서버가 지원하지 않을 때 발생하는 예외 처리
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ExceptionResponse> notAcceptableExceptionHandler(Exception exception) {
        return ResponseEntity
                .status(NOT_ACCEPTABLE)
                .body(buildResponse(NOT_ACCEPTABLE, exception));
    }

    // 클라이언트가 서버에서 지원하지 않는 미디어 타입으로 요청할 때 발생하는 예외를 처리합니다.
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> unSupportedMediaTypeExceptionHandler(Exception exception) {
        return ResponseEntity
                .status(UNSUPPORTED_MEDIA_TYPE)
                .body(buildResponse(UNSUPPORTED_MEDIA_TYPE, exception));
    }

    // 서버가 요청을 처리할 수 없는 상황에서 발생하는 예외들을 처리합니다.
    @ExceptionHandler({
            MissingPathVariableException.class,     // 경로 변수 누락 시 발생
            ConversionNotSupportedException.class,  // 타입 변환이 지원되지 않을 때 발생
            HttpMessageNotWritableException.class,  // HTTP 메시지를 작성할 수 없을 때 발생
    })
    public ResponseEntity<ExceptionResponse> internalServerErrorExceptionHandler(Exception exception) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(buildResponse(INTERNAL_SERVER_ERROR, exception));
    }

    // 위에서 처리되지 않은 모든 일반적인 예외를 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> globalExceptionHandler(Exception exception) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(buildResponse(INTERNAL_SERVER_ERROR, exception));
    }

    // 기본 예외 응답 빌더 메서드
    private ExceptionResponse buildResponse(HttpStatus status, Exception exception) {
        return ExceptionResponse.builder()
                .status(status.value())
                .message(exception.getLocalizedMessage())
                .build();
    }

    // 유효성 검사 실패 시 발생하는 예외를 처리하며, 설정된 메시지를 반환
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> validationFailedExceptionHandler(MethodArgumentNotValidException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(buildValidationFailedResponse(BAD_REQUEST, exception));
    }

    // 유효성 검사 실패 응답을 빌드하는 메서드
    private ExceptionResponse buildValidationFailedResponse(HttpStatus status, MethodArgumentNotValidException exception) {
        return ExceptionResponse.builder()
                .status(status.value())
                .message(exception.getFieldErrors().get(0).getDefaultMessage())
                .build();
    }

    // 파일 업로드 시, 파일 크기가 설정된 최대 크기를 초과할 때 발생하는 예외를 처리
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ExceptionResponse> handleMaxSizeException(MaxUploadSizeExceededException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(buildResponse(BAD_REQUEST, exception));
    }
}
