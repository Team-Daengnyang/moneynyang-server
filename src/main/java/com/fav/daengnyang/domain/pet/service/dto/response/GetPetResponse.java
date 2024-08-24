package com.fav.daengnyang.domain.pet.service.dto.response;

import com.fav.daengnyang.domain.pet.entity.Pet;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class GetPetResponse {
   private Long petId;
   private String petName;
   private String petBirth;
   private String petType;
   private String petSex;
   private String specie;
   private String petImage;

   @Builder
    private GetPetResponse(Long petId, String petName, String petBirth, String petType, String petSex, String specie, String petImage) {
       this.petId = petId;
       this.petName = petName;
       this.petBirth = petBirth;
       this.petType = petType;
       this.petSex = petSex;
       this.specie = specie;
       this.petImage = petImage;
   }

   public static GetPetResponse createGetPetResponse(Pet pet, String petImage) {
       return builder()
               .petId(pet.getPetId())
               .petName(pet.getPetName())
               .petBirth(pet.getPetBirth())
               .petType(pet.getPetType())
               .petSex(pet.getPetSex())
               .specie(pet.getSpecie())
               .petImage(petImage)
               .build();
   }
}
