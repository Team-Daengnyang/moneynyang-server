package com.fav.daengnyang.domain.target.controller;

import com.fav.daengnyang.domain.target.service.dto.TargetService;
import com.fav.daengnyang.domain.target.service.dto.request.CreateTargetRequest;
import com.fav.daengnyang.domain.target.service.dto.response.TargetResponse;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/targets")
@RestController
public class TargetController {

    private final TargetService targetService;

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
}
