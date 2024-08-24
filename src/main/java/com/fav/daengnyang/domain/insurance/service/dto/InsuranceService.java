package com.fav.daengnyang.domain.insurance.service.dto;

import com.fav.daengnyang.domain.insurance.entity.Insurance;
import com.fav.daengnyang.domain.insurance.repository.InsuranceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;

    public List<Insurance> getListInsurance(String petType){
        return insuranceRepository.findByPetType(petType);
    }
}
