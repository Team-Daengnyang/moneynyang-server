package com.fav.daengnyang.domain.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.member.service.MemberService;
import com.fav.daengnyang.domain.member.service.dto.request.CheckRequest;
import com.fav.daengnyang.domain.member.service.dto.request.CreatedRequest;
import com.fav.daengnyang.domain.member.service.dto.request.LoginRequest;
import com.fav.daengnyang.domain.member.service.dto.response.LoginResponse;
import com.fav.daengnyang.domain.member.service.dto.response.MemberInfoResponse;
import com.fav.daengnyang.domain.pet.service.dto.response.CheckResponse;
import com.fav.daengnyang.global.auth.AuthService;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.auth.utils.JWTProvider;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/members")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    // 회원 가입
   @PostMapping
    public SuccessResponse<?> createMember(@RequestBody @Valid CreatedRequest createdRequest) throws JsonProcessingException {
           LoginResponse loginResponse = memberService.createMember(createdRequest);
           return SuccessResponse.ok(loginResponse);
   }

   // 로그인
    @PostMapping("/login")
    public SuccessResponse<?> login(@RequestBody @Valid LoginRequest loginRequest){
        LoginResponse loginResponse = authService.login(loginRequest);
       return SuccessResponse.ok(loginResponse);
    }

    // 사용자 정보 조회
    @GetMapping("/info")
    public SuccessResponse<MemberInfoResponse> getMemberInfo(@AuthenticationPrincipal MemberPrincipal memberPrincipal) throws JsonProcessingException {
        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(memberPrincipal.getMemberId());
        return SuccessResponse.ok(memberInfoResponse);
    }

    // 이메일 중복 체크
    @PostMapping("/check")
    public SuccessResponse<?> checkDuplicateEmail(@RequestBody CheckRequest checkRequest) throws JsonProcessingException {

       CheckResponse response = memberService.checkDuplicateEmail(checkRequest.getEmail());
       return SuccessResponse.ok(response);
    }
    
}
