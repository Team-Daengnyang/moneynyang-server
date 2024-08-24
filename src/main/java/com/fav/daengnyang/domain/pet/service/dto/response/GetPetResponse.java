package com.fav.daengnyang.domain.pet.service.dto.response;

import com.fav.daengnyang.domain.pet.entity.Pet;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetPetResponse {
   private Pet pet;
   private String petImage;

   @Builder
    private GetPetResponse(Pet pet, String petImage) {
       this.pet = pet;
       this.petImage = petImage;
   }

   public static GetPetResponse createGetPetResponse(Pet pet, String petImage) {
       return builder().pet(pet)
               .petImage(petImage).build();
   }
}
