package com.fav.daengnyang.domain.member.controller;

import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.service.dto.MemberService;
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
    public ResponseEntity<?> createMember(@RequestBody Member member) {

       // 1. 금융 API 연결
       // 2. DB에 회원 정보 저장
       // 3. 결과 리턴
       return ResponseEntity.status(HttpStatus.OK);
   }
}
