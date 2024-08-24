package com.fav.daengnyang.domain.insurance.controller;

import com.fav.daengnyang.domain.insurance.service.dto.InsuranceService;
import com.fav.daengnyang.domain.insurance.service.dto.response.InsuranceResponse;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
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
    public SuccessResponse<?> getInsurances(@RequestParam("petType") String petType) {
        List<InsuranceResponse> response = insuranceService.getListInsurance(petType);
        return SuccessResponse.ok(response);
    }
}
