package com.fav.daengnyang.domain.insurance.controller;

import com.fav.daengnyang.domain.insurance.service.InsuranceService;
import com.fav.daengnyang.domain.insurance.service.dto.response.InsuranceDetailResponse;
import com.fav.daengnyang.domain.insurance.service.dto.response.InsuranceResponse;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/insurances")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class InsuranceController {
    private final InsuranceService insuranceService;

    @GetMapping
    public SuccessResponse<?> getInsurances(@RequestParam("insuranceType") String insuranceType) {
        List<InsuranceResponse> response = insuranceService.getListInsurance(insuranceType);
        return SuccessResponse.ok(response);
    }

    @GetMapping("/{insuranceId}")
    public SuccessResponse<?> getInsuranceById(@PathVariable("insuranceId") Long insuranceId) {
        InsuranceDetailResponse response = insuranceService.getInsurance(insuranceId);
        return SuccessResponse.ok(response);
    }
}
