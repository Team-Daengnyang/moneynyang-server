package com.fav.daengnyang.domain.pet.service;

import com.fav.daengnyang.domain.account.entity.Account;
import com.fav.daengnyang.domain.account.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

    public Long createPet(CreatedPetRequest createdPetRequest, Long memberId) {

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Pet pet = Pet.createPet(createdPetRequest, member);
        pet = petRepository.save(pet);
        return pet.getPetId();
    }

    public GetPetResponse getPet(Long memberId) {

        Pet pet = petRepository.findByMemberMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.PET_NOT_FOUND));
        log.info("pet 정보 불러오기");

        Account account = accountRepository.findByMemberMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        log.info("account 정보 불러오기");

        return GetPetResponse.createGetPetResponse(pet, account.getAccountImage());
    }
}