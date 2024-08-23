package com.fav.daengnyang.domain.pet.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fav.daengnyang.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    @Column(name = "spiece")
    private String spiece;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Pet(String petName, String petSex, String petType, String petBirth, String spiece, Member member) {
        this.petName = petName;
        this.petSex = petSex;
        this.petType = petType;
        this.petBirth = petBirth;
        this.spiece = spiece;
    }

//    public static Pet createPet(CreatedPetRequest createdPetRequest) {
//        return Pet.builder()
//                .petName()
//    }

}
