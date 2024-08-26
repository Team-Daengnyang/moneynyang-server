package com.fav.daengnyang.domain.targetDetail.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.targetDetail.service.dto.response.TargetDetailSummaryResponse;
import com.fav.daengnyang.domain.targetDetail.service.TargetDetailService;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookdata")
public class TargetDetailController {

    private final TargetDetailService bookdataService;

    @GetMapping("")
    public ResponseEntity<SuccessResponse<TargetDetailSummaryResponse>> getBookdataSummary(
            @RequestParam Long bankbookId, @RequestParam String accountNo) throws JsonProcessingException {
        TargetDetailSummaryResponse summary = bookdataService.getBookdataSummary(bankbookId, accountNo);
        return ResponseEntity.ok(SuccessResponse.ok(summary));
    }
}
