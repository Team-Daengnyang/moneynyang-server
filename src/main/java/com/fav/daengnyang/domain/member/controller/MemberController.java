package com.fav.daengnyang.domain.member.controller;

import com.fav.daengnyang.domain.member.service.dto.MemberService;
import com.fav.daengnyang.domain.member.service.dto.request.CreatedRequest;
import com.fav.daengnyang.domain.member.service.dto.response.LoginResponse;
import com.fav.daengnyang.domain.member.service.dto.request.MemberBankRequest;
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
           LoginResponse loginResponse = memberService.createMember(createdRequest);
           return SuccessResponse.ok(loginResponse);
       } catch (Exception e){
           log.debug(e.getMessage());
           return null;
       }
   }
}
