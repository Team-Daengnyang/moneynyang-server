package com.fav.daengnyang.domain.insurance.service.dto;

import com.fav.daengnyang.domain.insurance.entity.Insurance;
import com.fav.daengnyang.domain.insurance.repository.InsuranceRepository;
import com.fav.daengnyang.domain.insurance.service.dto.response.InsuranceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;

    public List<InsuranceResponse> getListInsurance(String petType){
        List<Insurance> insurances = insuranceRepository.findByPetType(petType);
        log.info("Number of insurances found: " + insurances.size());
        List<InsuranceResponse> response = new ArrayList<>();
        for (Insurance insurance : insurances) {
            response.add(InsuranceResponse.createInsuranceResponse(insurance));
        }
        return response;
    }
}
