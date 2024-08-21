package com.fav.daengnyang.domain.bankbook.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.bankbook.service.dto.BankbookService;
import com.fav.daengnyang.domain.bankbook.service.dto.request.BankbookRequest;
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
}