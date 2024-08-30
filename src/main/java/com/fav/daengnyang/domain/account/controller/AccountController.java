package com.fav.daengnyang.domain.account.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.account.service.AccountService;
import com.fav.daengnyang.domain.account.service.dto.request.*;
import com.fav.daengnyang.domain.account.service.dto.response.AccountCreateColorResponse;
import com.fav.daengnyang.domain.account.service.dto.response.AccountCreateResponse;
import com.fav.daengnyang.domain.account.service.dto.response.AccountInfoResponse;
import com.fav.daengnyang.domain.account.service.dto.response.AccountResponse;
import com.fav.daengnyang.domain.targetDetail.service.dto.response.AccountHistoryResponse;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse<AccountCreateResponse>> createAccount(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody AccountCreateRequest request) throws JsonProcessingException {
        AccountCreateResponse response = accountService.createAccount(request, memberPrincipal.getUserKey(), memberPrincipal.getMemberId());
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    @GetMapping("/inquire")
    public ResponseEntity<SuccessResponse<AccountResponse>> inquireAccount(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) throws JsonProcessingException {
        AccountResponse response = accountService.inquireAccount(memberPrincipal.getMemberId(), memberPrincipal.getUserKey());
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    @PatchMapping(value = "/update-color")
    public ResponseEntity<SuccessResponse<AccountResponse>> updateCustomColor(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody ColorUpdateRequest request) {
        AccountResponse response = accountService.updateAccountColor(memberPrincipal.getMemberId(), request.getNewColor());
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    @PatchMapping(value = "/update-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<?>> updateCustomImage(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
           ImageUpdateRequest request) {
        String response = accountService.updateAccountImage(memberPrincipal.getMemberId(), request.getAccoutImage());
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

    @PostMapping("/transfer")
    public SuccessResponse<?> getAccountTransfer(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @Valid @RequestBody TransferRequest transferRequest) {
        accountService.transferMoney(memberPrincipal, transferRequest);
        return SuccessResponse.ok();
    }

    @PatchMapping("/create-color")
    public ResponseEntity<SuccessResponse<AccountCreateColorResponse>> creatAccountColor(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody AccountCreateColorRequest request) {
        AccountCreateColorResponse response = accountService.createColorAccount(request, memberPrincipal.getMemberId());
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    @GetMapping("/name")
    public SuccessResponse<?> getRandomName(){
        HashMap<String, String> name = accountService.getRandomName();

        return SuccessResponse.ok(name);
    }
}