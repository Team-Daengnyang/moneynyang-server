package com.fav.daengnyang.domain.memberDetail.controller;

import com.fav.daengnyang.domain.memberDetail.service.dto.request.AnalyzeMonthlyRequest;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/memberDetails")
@Slf4j
@RequiredArgsConstructor
public class MemberDetailController {

    // 개인 소비 데이터 분석(이전 월과 비교)
    @PostMapping
    public SuccessResponse<?> analyzeSpendingMonthly(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody AnalyzeMonthlyRequest request){

        return SuccessResponse.ok();
    }
}
