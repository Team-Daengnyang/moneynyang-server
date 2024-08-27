package com.fav.daengnyang.domain.account.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.account.service.AccountService;
import com.fav.daengnyang.domain.account.service.dto.request.AccountRequest;
import com.fav.daengnyang.domain.account.service.dto.request.ColorUpdateRequest;
import com.fav.daengnyang.domain.account.service.dto.response.AccountCreateResponse;
import com.fav.daengnyang.domain.account.service.dto.response.AccountInfoResponse;
import com.fav.daengnyang.domain.account.service.dto.response.AccountResponse;
import com.fav.daengnyang.domain.targetDetail.service.dto.response.AccountHistoryResponse;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse<AccountCreateResponse>> createAccount(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody AccountRequest request) throws JsonProcessingException {
        AccountCreateResponse response = accountService.createAccount(request, memberPrincipal.getUserKey(), memberPrincipal.getMemberId());
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    @GetMapping("/inquire")
    public ResponseEntity<SuccessResponse<AccountResponse>> inquireAccount(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) throws JsonProcessingException {
        AccountResponse response = accountService.inquireAccount(memberPrincipal.getMemberId(), memberPrincipal.getUserKey());
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    @PatchMapping("/update-color")
    public ResponseEntity<SuccessResponse<AccountResponse>> updateCustomColor(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody ColorUpdateRequest request) {
        AccountResponse response = accountService.updateAccountColor(memberPrincipal.getMemberId(), request.getNewColor());
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    @GetMapping("/history")
    public ResponseEntity<SuccessResponse<List<AccountHistoryResponse>>> getAccountHistory(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) throws JsonProcessingException {
        List<AccountHistoryResponse> response = accountService.getAccountHistory(memberPrincipal.getUserKey());
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    @GetMapping("/info")
    public ResponseEntity<SuccessResponse<AccountInfoResponse>> getAccountInfo(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) throws JsonProcessingException {
        AccountInfoResponse response = accountService.getAccountInfo(memberPrincipal.getMemberId(), memberPrincipal.getUserKey());
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }
}