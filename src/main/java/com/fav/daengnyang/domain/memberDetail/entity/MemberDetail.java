package com.fav.daengnyang.domain.memberDetail.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fav.daengnyang.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "member_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_detail_id")
    private Long memberDetailId;

    @Column(name = "pet")
    private int pet;

    @Column(name = "food")
    private int food;

    @Column(name = "shopping")
    private int shopping;

    @Column(name = "transportation")
    private int transportation;

    @Column(name = "others")
    private int others;

    @Column(name = "total")
    private int total;

    @Column(name = "pet_pay_count")
    private int petPayCount;

    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    private MemberDetail(int petPayCount, int pet, int food, int shopping, int transportation, int others, int total, LocalDate date, Member member) {
        this.pet = pet;
        this.food = food;
        this.shopping = shopping;
        this.transportation = transportation;
        this.others = others;
        this.total = total;
        this.date = date;
        this.member = member;
        this.petPayCount = petPayCount;
    }

    public static MemberDetail createMemberDetail(int petPayCount, int pet, int food, int shopping, int transportation, int others, LocalDate date, Member member) {
        return MemberDetail.builder()
                .pet(pet)
                .food(food)
                .shopping(shopping)
                .transportation(transportation)
                .others(others)
                .date(date)
                .member(member)
                .petPayCount(petPayCount)
                .build();
    }
}
