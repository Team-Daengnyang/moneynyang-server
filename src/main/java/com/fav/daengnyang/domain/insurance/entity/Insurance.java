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

    @Column(name = "insurance_type")
    private String insuranceType;

    @Column(name = "title")
    private String title;

    @Column(name = "company_image")
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
    private Insurance(String title, String companyImage, String insuranceType, String url, String price, String comment, String summary) {
        this.title = title;
        this.companyImage = companyImage;
        this.url = url;
        this.price = price;
        this.comment = comment;
        this.summary = summary;
        this.insuranceType = insuranceType;
    }
}
