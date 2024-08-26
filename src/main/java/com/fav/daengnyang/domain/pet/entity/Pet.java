package com.fav.daengnyang.domain.pet.entity;

import com.fav.daengnyang.domain.member.entity.Member;
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

    @Column(name = "specie")
    private String specie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Pet(String petName, String petGender, String petType, String petBirth, String specie, Member member) {
        this.petName = petName;
        this.petGender = petGender;
        this.petType = petType;
        this.petBirth = petBirth;
        this.specie = specie;
        this.member = member;
    }
}
