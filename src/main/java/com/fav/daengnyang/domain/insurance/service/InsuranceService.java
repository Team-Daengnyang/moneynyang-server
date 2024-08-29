package com.fav.daengnyang.domain.insurance.service;

import com.fav.daengnyang.domain.insurance.entity.Insurance;
import com.fav.daengnyang.domain.insurance.repository.InsuranceRepository;
import com.fav.daengnyang.domain.insurance.service.dto.response.InsuranceDetailResponse;
import com.fav.daengnyang.domain.insurance.service.dto.response.InsuranceResponse;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import com.fav.daengnyang.domain.pet.entity.Pet;
import com.fav.daengnyang.domain.pet.repository.PetRepository;
import com.fav.daengnyang.global.exception.CustomException;
import com.fav.daengnyang.global.exception.ErrorCode;
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
    private final PetRepository petRepository;

    public List<InsuranceResponse> getListInsurance(Long memberId){
        Pet pet = petRepository.findByMemberMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.PET_NOT_FOUND));
        List<Insurance> insurances = insuranceRepository.findByInsuranceType(pet.getPetType());
        log.info("Number of insurances found: " + insurances.size());
        List<InsuranceResponse> response = new ArrayList<>();
        for (Insurance insurance : insurances) {
            response.add(InsuranceResponse.createInsuranceResponse(insurance));
        }
        return response;
    }

    public InsuranceDetailResponse getInsurance(Long insuranceId){
        Insurance insurance = insuranceRepository.findInsuranceByInsuranceId(insuranceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_INSURANCE));
        return InsuranceDetailResponse.createInsuranceResponse(insurance);
    }
}
