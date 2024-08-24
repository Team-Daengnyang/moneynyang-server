package com.fav.daengnyang.domain.pet.service.dto;

import com.fav.daengnyang.domain.bankbook.entity.Bankbook;
import com.fav.daengnyang.domain.bankbook.repository.BankbookRepository;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import com.fav.daengnyang.domain.pet.entity.Pet;
import com.fav.daengnyang.domain.pet.repository.PetRepository;
import com.fav.daengnyang.domain.pet.service.dto.request.CreatedPetRequest;
import com.fav.daengnyang.domain.pet.service.dto.response.GetPetResponse;
import com.fav.daengnyang.global.exception.CustomException;
import com.fav.daengnyang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final MemberRepository memberRepository;
    private final BankbookRepository bankbookRepository;

    public Long createPet(CreatedPetRequest createdPetRequest, Long memberId){

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Pet pet = Pet.createPet(createdPetRequest, member);
        pet = petRepository.save(pet);
        return pet.getPetId();
    }

    public GetPetResponse getPet(Long memberId){

        Pet pet = petRepository.findByMemberMemberId(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.PET_NOT_FOUND));
        log.info("pet 정보 불러오기");
        Bankbook bankbook = bankbookRepository.findByMemberMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.BANKBOOK_NOT_FOUND));
        log.info("bankbook 정보 불러오기");
        return GetPetResponse.createGetPetResponse(pet, bankbook.getBankbookImage());
    }

}
