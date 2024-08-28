package com.fav.daengnyang.domain.targetDetail.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.target.service.dto.response.TargetDetailResponse;
import com.fav.daengnyang.domain.targetDetail.service.dto.request.TransactionDetailRequest;
import com.fav.daengnyang.domain.targetDetail.service.dto.response.TargetDetailSummaryResponse;
import com.fav.daengnyang.domain.targetDetail.service.TargetDetailService;
import com.fav.daengnyang.domain.targetDetail.service.dto.response.TransactionDetailResponse;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/target-detail")
public class TargetDetailController {

    private final TargetDetailService targetDetailService;

    @GetMapping("")
    public ResponseEntity<SuccessResponse<List<TransactionDetailResponse>>> getTransactionDetails(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestParam String accountNo,
            @RequestParam String startDate,
            @RequestParam String endDate) throws JsonProcessingException {
        TransactionDetailRequest request = new TransactionDetailRequest(startDate, endDate, accountNo);

        List<TransactionDetailResponse> responses = targetDetailService.getTransactionDetails(accountNo, memberPrincipal.getUserKey(), request);
        return ResponseEntity.ok(SuccessResponse.ok(responses));
    }
}