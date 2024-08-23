package com.fav.daengnyang.domain.pet.service.dto;

import com.fav.daengnyang.domain.pet.entity.Pet;
import com.fav.daengnyang.domain.pet.repository.PetRepository;
import com.fav.daengnyang.domain.pet.service.dto.request.CreatedPetRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public void createPet(CreatedPetRequest createdPetRequest){
        Pet pet = Pet.createPet(createdPetRequest);
        petRepository.save(pet);
    }
}
