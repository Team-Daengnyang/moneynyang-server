package com.fav.daengnyang.domain.member.controller;

import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.service.dto.MemberService;
import com.fav.daengnyang.domain.member.service.dto.response.LoginResponse;
import com.fav.daengnyang.domain.member.service.dto.response.MemberBankResponse;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
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
    public SuccessResponse<?> createMember(@RequestBody Member member) {

       try{
           log.debug(" // 1. 금융 API 연결");
           // 1. 금융 API 연결
           MemberBankResponse memberBankResponse = memberService.createMemberBank(member);
           // 2. DB에 회원 정보 저장
           memberService.save(member);
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
