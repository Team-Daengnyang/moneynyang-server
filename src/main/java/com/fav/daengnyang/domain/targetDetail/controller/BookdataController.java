package com.fav.daengnyang.domain.targetDetail.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.targetDetail.service.dto.response.BookdataSummaryResponse;
import com.fav.daengnyang.domain.targetDetail.service.BookdataService;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookdata")
public class BookdataController {

    private final BookdataService bookdataService;

    @GetMapping("")
    public ResponseEntity<SuccessResponse<BookdataSummaryResponse>> getBookdataSummary(
            @RequestParam Long bankbookId, @RequestParam String accountNo) throws JsonProcessingException {
        BookdataSummaryResponse summary = bookdataService.getBookdataSummary(bankbookId, accountNo);
        return ResponseEntity.ok(SuccessResponse.ok(summary));
    }
}
