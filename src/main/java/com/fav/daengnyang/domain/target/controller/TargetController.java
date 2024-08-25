package com.fav.daengnyang.domain.target.controller;

import com.fav.daengnyang.domain.target.service.TargetService;
import com.fav.daengnyang.domain.target.service.TargetSummaryService;
import com.fav.daengnyang.domain.target.service.dto.request.CreateTargetRequest;
import com.fav.daengnyang.domain.target.service.dto.response.TargetResponse;
import com.fav.daengnyang.domain.target.service.dto.response.TargetSummaryResponse;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/targets")
@RestController
public class TargetController {

    private final TargetService targetService;
    private final TargetSummaryService targetSummaryService;

    // 목표 등록
    // 성공, 실패 여부만 return
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SuccessResponse<?> createTarget(
            @RequestParam(value = "memberId") Long memberId,
            @Valid @RequestBody CreateTargetRequest request){

        Long targetId = targetService.createTarget(request, memberId);

        return SuccessResponse.created(targetId);
    }

    // 유저의 모든 목표 리스트로 조회하기
    @GetMapping
    public SuccessResponse<List<TargetResponse>> getTargets(@RequestParam(value = "memberId") Long memberId){

        List<TargetResponse> targets = targetService.findTargets(memberId);
        return SuccessResponse.ok(targets);
    }

    // 지금까지의 덕질 정보 가져오기
    @GetMapping("/summary")
    public SuccessResponse<?> getTargetSummary(@AuthenticationPrincipal MemberPrincipal memberPrincipal){
        Long memberId = memberPrincipal.getMemberId();

        // service 호출
        TargetSummaryResponse summary = targetSummaryService.getTargetSummary(memberId);

        return SuccessResponse.ok(summary);
    }

}
