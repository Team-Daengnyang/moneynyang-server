package com.fav.daengnyang.domain.bankbook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Bankbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountTypeUniqueNo; // 상품 고유번호
    private String userKey; // 사용자 키
    private String customImageUrl; // 커스텀 이미지 URL
    private String customColor; // 커스텀 색상

    public Bankbook(String accountTypeUniqueNo, String userKey, String customImageUrl, String customColor) {
        this.accountTypeUniqueNo = accountTypeUniqueNo;
        this.userKey = userKey;
        this.customImageUrl = customImageUrl;
        this.customColor = customColor;
    }

    public void setCustomColor(String customColor) {
        this.customColor = customColor;
    }
}
