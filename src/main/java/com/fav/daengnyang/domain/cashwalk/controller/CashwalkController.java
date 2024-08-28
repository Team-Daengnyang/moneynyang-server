package com.fav.daengnyang.domain.cashwalk.controller;

import com.fav.daengnyang.domain.cashwalk.entity.Cashwalk;
import com.fav.daengnyang.domain.cashwalk.service.dto.CashwalkService;
import com.fav.daengnyang.domain.cashwalk.service.dto.request.CashwalkRequest;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/cashwalks")
@RestController
public class CashwalkController {

    private final CashwalkService cashwalkService;

    // 일지 등록
    @PostMapping
    public SuccessResponse<?> createCashwalk(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @Valid @RequestBody CashwalkRequest cashwalkRequest){

        // Service 호출
        Long cashwalkId = cashwalkService.createCashwalk(memberPrincipal.getMemberId(), cashwalkRequest);
        return SuccessResponse.created(cashwalkId);
    }

}
