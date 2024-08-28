package com.fav.daengnyang.domain.pet.service.dto.request;

import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class CreatedPetRequest {

    @NotBlank
    private String petName;
    @NotBlank
    private String petGender;
    @NotBlank
    private String petType;
    private MultipartFile petImage;
    private String petBirth;
    private String specie;
}
