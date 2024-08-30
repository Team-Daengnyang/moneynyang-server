package com.fav.daengnyang.domain.cashwalk.controller;

import com.fav.daengnyang.domain.cashwalk.service.CashwalkService;
import com.fav.daengnyang.domain.cashwalk.service.dto.request.CreateCashwalkRequest;
import com.fav.daengnyang.domain.cashwalk.service.dto.response.CashwalkResponse;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/cashwalks")
@RestController
public class CashwalkController {

    private final CashwalkService cashwalkService;

    // 일지 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<?> createCashwalk(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @Valid CreateCashwalkRequest createCashwalkRequest) throws IOException {

        // Service 호출
        Long cashwalkId = cashwalkService.createCashwalk(memberPrincipal.getMemberId(), createCashwalkRequest);
        return SuccessResponse.created(cashwalkId);
    }

    // 이 달의 작성된 일지 조회
    @GetMapping("/month")
    public SuccessResponse<List<CashwalkResponse>> getCashwalksForMonth(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestParam String date){

        // Service 호출
        List<CashwalkResponse> responses = cashwalkService.getCashwalksForMonth(memberPrincipal.getMemberId(), date);
        return SuccessResponse.ok(responses);
    }
}
