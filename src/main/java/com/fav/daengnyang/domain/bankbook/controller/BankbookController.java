package com.fav.daengnyang.domain.bankbook.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.bankbook.service.BankbookService;
import com.fav.daengnyang.domain.bankbook.service.dto.request.BankbookRequest;
import com.fav.daengnyang.domain.bankbook.service.dto.request.ColorUpdateRequest;
import com.fav.daengnyang.domain.bankbook.service.dto.response.BankbookCreateResponse;
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
    public ResponseEntity<SuccessResponse<BankbookCreateResponse>> createBankbook(@RequestBody BankbookRequest request) throws JsonProcessingException {
        BankbookCreateResponse response = bankbookService.createBankbook(request);
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    @PostMapping("/inquire")
    public ResponseEntity<SuccessResponse<BankbookResponse>> inquireBankbook(@RequestParam String bankbookNumber) throws JsonProcessingException {
        BankbookResponse response = bankbookService.inquireBankbook(bankbookNumber);
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    @PatchMapping("/update-color")
    public ResponseEntity<SuccessResponse<BankbookResponse>> updateCustomColor(
            @RequestParam String bankbookNumber,
            @RequestBody ColorUpdateRequest request) {
        BankbookResponse response = bankbookService.updateBankbookColor(bankbookNumber, request.getNewColor());
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }
}
