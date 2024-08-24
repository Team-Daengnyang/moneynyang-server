package com.fav.daengnyang.domain.pet.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.pet.service.dto.request.CreatedPetRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pet")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "pet_name")
    private String petName;

    @Column(name = "pet_sex")
    private String petSex;

    @Column(name = "pet_type")
    private String petType;

    @Column(name = "pet_birth")
    private String petBirth;

    @Column(name = "specie")
    private String specie;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Pet(String petName, String petSex, String petType, String petBirth, String specie, Member member) {
        this.petName = petName;
        this.petSex = petSex;
        this.petType = petType;
        this.petBirth = petBirth;
        this.specie = specie;
        this.member = member;
    }

    public static Pet createPet(CreatedPetRequest createdPetRequest, Member member) {
        return Pet.builder()
                .petName(createdPetRequest.getPetName())
                .petSex(createdPetRequest.getPetSex())
                .petType(createdPetRequest.getPetType())
                .petBirth(createdPetRequest.getPetBirth())
                .specie(createdPetRequest.getSpecie())
                .member(member)
                .build();
    }

}
