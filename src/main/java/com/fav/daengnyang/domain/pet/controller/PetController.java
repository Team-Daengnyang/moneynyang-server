package com.fav.daengnyang.domain.pet.controller;

import com.fav.daengnyang.domain.pet.service.PetService;
import com.fav.daengnyang.domain.pet.service.dto.request.CreatedPetRequest;
import com.fav.daengnyang.domain.pet.service.dto.request.UpdatedPetRequest;
import com.fav.daengnyang.domain.pet.service.dto.response.GetPetResponse;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PetController {

    private final PetService petService;

    // 반려동물 정보 저장
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<?> createPet(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @Valid CreatedPetRequest createdPetRequest) {
        log.info("created" + createdPetRequest);
        Long petId = petService.createPet(createdPetRequest, memberPrincipal.getMemberId());
        return SuccessResponse.created(petId);
    }

    // 반려동물 정보 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<GetPetResponse>> getPet(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        GetPetResponse response = petService.getPet(memberPrincipal.getMemberId());
        log.info("response: {}", response);
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }

    // 반려동물 정보 수정
    @PatchMapping
    public SuccessResponse<?> updatePet(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @Valid UpdatedPetRequest updatedPetRequest){
        petService.updatePet(updatedPetRequest, memberPrincipal.getMemberId());
        return SuccessResponse.update();
    }

}
