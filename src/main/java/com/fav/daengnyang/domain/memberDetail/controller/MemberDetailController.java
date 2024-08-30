package com.fav.daengnyang.domain.memberDetail.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.memberDetail.service.dto.MemberDetailService;
import com.fav.daengnyang.domain.memberDetail.service.dto.request.AnalyzeMonthlyRequest;
import com.fav.daengnyang.domain.memberDetail.service.dto.response.AnalyzeMonthlyResponse;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member-detail")
@Slf4j
@RequiredArgsConstructor
public class MemberDetailController {
    private final MemberDetailService memberDetailService;

    // 개인 소비 데이터 분석(이전 월과 비교)
    @PostMapping
    public SuccessResponse<?> analyzeSpendingMonthly(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody AnalyzeMonthlyRequest request) throws JsonProcessingException {

        AnalyzeMonthlyResponse response = memberDetailService.analyzeMonthly(memberPrincipal, request);
        return SuccessResponse.ok(response);
    }
}
