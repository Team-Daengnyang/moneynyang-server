package com.fav.daengnyang.global.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fav.daengnyang.global.dto.response.ExceptionResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            setErrorResponse(e.getErrorCode().getHttpStatus(), response, e);
        } catch (Exception e) {
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, e);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) throws IOException {
        log.error("[ExceptionHandlerFilter] errMsg : " + ex.getMessage());

        response.setStatus(status.value());
        response.setContentType("application/json; charset=UTF-8");


        // 에러 응답을 위한 Map 생성
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.value());
        errorResponse.put("message", ex.getMessage());

        // JSON 형태로 변환하여 응답에 쓰기
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(errorResponse)
        );
    }

}
