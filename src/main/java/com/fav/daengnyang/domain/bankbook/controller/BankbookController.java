package com.fav.daengnyang.domain.bankbook.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.bankbook.service.dto.BankbookService;
import com.fav.daengnyang.domain.bankbook.service.dto.request.BankbookRequest;
import com.fav.daengnyang.domain.bankbook.service.dto.request.ColorUpdateRequest;
import com.fav.daengnyang.domain.bankbook.service.dto.response.BankbookResponse;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bankbook")
@RequiredArgsConstructor
public class BankbookController {

    private final BankbookService bankbookService;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse<BankbookResponse>> createBankbook(@RequestBody BankbookRequest request) throws JsonProcessingException {
        BankbookResponse response = bankbookService.createBankbook(request);
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    @PostMapping("/inquire")
    public ResponseEntity<SuccessResponse<BankbookResponse>> inquireBankbook(@RequestBody String accountNo) throws JsonProcessingException {
        BankbookResponse response = bankbookService.inquireBankbook(accountNo);
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    // 커스텀 색상 업데이트
    @PatchMapping("/update-color")
    public ResponseEntity<SuccessResponse<BankbookResponse>> updateCustomColor(
            @RequestParam String userKey,
            @RequestBody ColorUpdateRequest request) {
        BankbookResponse response = null;
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }
}