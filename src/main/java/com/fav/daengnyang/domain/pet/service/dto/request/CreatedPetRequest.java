package com.fav.daengnyang.domain.pet.service.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreatedPetRequest {

    @NotBlank
    private String petName;
    @NotBlank
    private String petGender;
    @NotBlank
    private String petType;
    private String petBirth;
    private String specie;
}
