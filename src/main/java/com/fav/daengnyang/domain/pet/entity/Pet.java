package com.fav.daengnyang.domain.pet.entity;

import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.pet.service.dto.request.CreatedPetRequest;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "pet_gender")
    private String petGender;

    @Column(name = "pet_type")
    private String petType;

    @Column(name = "pet_birth")
    private String petBirth;

    @Column(name = "pet_image")
    private String petImage;

    @Column(name = "specie")
    private String specie;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    private Pet(String petName, String petGender, String petType, String petBirth, String specie, String petImage, Member member) {
        this.petName = petName;
        this.petGender = petGender;
        this.petType = petType;
        this.petBirth = petBirth;
        this.specie = specie;
        this.petImage = petImage;
        this.member = member;
    }

    public static Pet createPet(CreatedPetRequest createdPetRequest, Member member) {
        return Pet.builder()
                .petName(createdPetRequest.getPetName())
                .petGender(createdPetRequest.getPetGender())
                .petType(createdPetRequest.getPetType())
                .petBirth(createdPetRequest.getPetBirth())
                .specie(createdPetRequest.getSpecie())
                .petImage(createdPetRequest.getPetImage())
                .member(member)
                .build();
    }
}
