package com.fav.daengnyang.domain.account.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.account.service.dto.request.AccountRequest;
import com.fav.daengnyang.domain.account.service.dto.request.ColorUpdateRequest;
import com.fav.daengnyang.domain.account.service.dto.response.AccountCreateResponse;
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
@RequestMapping("/bankbook")
@RequiredArgsConstructor
public class AccountController {

    private final BankbookService bankbookService;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse<AccountCreateResponse>> createBankbook(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody AccountRequest request) throws JsonProcessingException {
        AccountCreateResponse response = bankbookService.createBankbook(request, memberPrincipal.getUserKey());
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }
    @GetMapping("/inquire")
    public ResponseEntity<SuccessResponse<AccountResponse>> inquireBankbook(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) throws JsonProcessingException {

        AccountResponse response = bankbookService.inquireBankbook(memberPrincipal.getMemberId());
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    @PatchMapping("/update-color")
    public ResponseEntity<SuccessResponse<AccountResponse>> updateCustomColor(
            @RequestParam String bankbookNumber,
            @RequestBody ColorUpdateRequest request) {
        AccountResponse response = bankbookService.updateBankbookColor(bankbookNumber, request.getNewColor());
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    @GetMapping("/history")
    public ResponseEntity<SuccessResponse<List<AccountHistoryResponse>>> getBankbookHistory(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) throws JsonProcessingException {
        List<AccountHistoryResponse> response = bankbookService.getBankbookHistory(memberPrincipal.getUserKey());
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }
}
