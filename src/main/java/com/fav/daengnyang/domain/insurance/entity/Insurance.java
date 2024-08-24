package com.fav.daengnyang.domain.insurance.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "insurance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "insurance_id")
    private Long insuranceId;

    @Column(name = "petType")
    private String petType;

    @Column(name = "title")
    private String title;

    @Column(name = "companyImage")
    private String companyImage;

    @Column(name = "url")
    private String url;

    @Column(name = "price")
    private String price;

    @Column(name = "comment")
    private String comment;

    @Column(name = "summary")
    private String summary;

    @Builder
    private Insurance(String petType, String title, String companyImage, String url, String price, String comment, String summary) {
        this.petType = petType;
        this.title = title;
        this.companyImage = companyImage;
        this.url = url;
        this.price = price;
        this.comment = comment;
        this.summary = summary;
    }
}
