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

       return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.CREATED, "회원가입에 성공했습니다.", null));
   }
}
