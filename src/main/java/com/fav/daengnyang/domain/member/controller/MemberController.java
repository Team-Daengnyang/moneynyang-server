package com.fav.daengnyang.domain.member.controller;

import com.fav.daengnyang.domain.member.service.dto.MemberService;
import com.fav.daengnyang.domain.member.service.dto.request.CreatedRequest;
import com.fav.daengnyang.domain.member.service.dto.response.LoginResponse;
import com.fav.daengnyang.domain.member.service.dto.response.MemberBankResponse;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberService memberService;

    // 회원 가입
   @PostMapping
    public SuccessResponse<?> createMember(@RequestBody @Valid CreatedRequest createdRequest) {

       try{

           // 1. 금융 API 연결
           MemberBankResponse memberBankResponse = memberService.createMemberBank(createdRequest);
           // 2. DB에 회원 정보 저장 --> service 단으로
           memberService.save(createdRequest);
           // 3. 예금 계좌 개설

           // 4. accessToken 생성
           LoginResponse loginResponse = memberService.createAccessToken(memberBankResponse);
           // 5. 결과 return
           return SuccessResponse.ok(loginResponse);
       } catch (Exception e){
           log.debug(e.getMessage());
           return null;
       }
   }
}
