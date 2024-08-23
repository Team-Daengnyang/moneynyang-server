package com.fav.daengnyang.domain.target.controller;

import com.fav.daengnyang.domain.target.service.dto.TargetService;
import com.fav.daengnyang.domain.target.service.dto.response.TargetResponse;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/targets")
@RestController
public class TargetController {

    private final TargetService targetService;

    // 유저의 모든 목표 리스트로 조회하기
    @GetMapping
    public SuccessResponse<List<TargetResponse>> getTargets(@RequestParam(value = "memberId") Long memberId){

        List<TargetResponse> targets = targetService.findTargets(memberId);
        return SuccessResponse.ok(targets);
    }
}
