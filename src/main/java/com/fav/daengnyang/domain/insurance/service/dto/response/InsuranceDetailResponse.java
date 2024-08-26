package com.fav.daengnyang.domain.insurance.service.dto.response;

import com.fav.daengnyang.domain.insurance.entity.Insurance;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InsuranceDetailResponse {

    private Long insuranceId;
    private String insuranceType;
    private String title;
    private String companyImage;
    private String url;
    private String price;
    private String comment;
    private String summary;

    @Builder
    private InsuranceDetailResponse(Long insuranceId, String insuranceType, String title, String companyImage, String url, String price, String comment, String summary) {
        this.insuranceId = insuranceId;
        this.insuranceType = insuranceType;
        this.title = title;
        this.companyImage = companyImage;
        this.url = url;
        this.price = price;
        this.comment = comment;
        this.summary = summary;
    }

    public static InsuranceDetailResponse createInsuranceResponse(Insurance insurance) {
        return InsuranceDetailResponse.builder()
                .insuranceId(insurance.getInsuranceId())
                .insuranceType(insurance.getInsuranceType())
                .title(insurance.getTitle())
                .companyImage(insurance.getCompanyImage())
                .url(insurance.getUrl())
                .price(insurance.getPrice())
                .comment(insurance.getComment())
                .summary(insurance.getSummary())
                .build();
    }
}
