package com.fav.daengnyang.domain.insurance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "insurance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "insurance_id")
    private Long insuranceId;

    @Column(name = "insurance_age")
    private String insuranceAge;

    @Column(name = "summary")
    private String summary;

    @Column(name = "comment")
    private String comment;

    @Column(name = "price")
    private String price;

    @Column(name = "title")
    private String title;

    @Column(name = "company_image")
    private String companyImage;

    @Column(name = "insurance_type")
    private String insuranceType;

    @Column(name = "url")
    private String url;

    @Builder
    private Insurance(String insuranceAge, String summary, String comment, String price, String title, String companyImage, String insuranceType, String url) {
        this.insuranceAge = insuranceAge;
        this.summary = summary;
        this.comment = comment;
        this.price = price;
        this.title = title;
        this.companyImage = companyImage;
        this.insuranceType = insuranceType;
        this.url = url;
    }
}
