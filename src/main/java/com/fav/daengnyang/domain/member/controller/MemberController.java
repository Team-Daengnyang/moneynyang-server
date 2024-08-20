package com.fav.daengnyang.domain.member.controller;

import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.service.dto.MemberService;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
           // 1. 금융 API 연결
           memberService.createMemberBank(member);
           // 2. DB에 회원 정보 저장
           memberService.save(member);
           // 3. 예금 계좌 개설

           // 4. 결과 리턴
           return ResponseEntity.status(HttpStatus.OK);
       } catch (Exception e){
           log.debug(e.getMessage());
       }

   }
}
