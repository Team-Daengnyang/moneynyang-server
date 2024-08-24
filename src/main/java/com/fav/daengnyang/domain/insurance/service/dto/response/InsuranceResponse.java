package com.fav.daengnyang.domain.insurance.service.dto.response;

import com.fav.daengnyang.domain.insurance.entity.Insurance;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InsuranceResponse {

    private Long insuranceId;
    private String title;
    private String companyImage;
    private String price;


    @Builder
    private InsuranceResponse(Long insuranceId, String title, String companyImage, String price) {
        this.insuranceId = insuranceId;
        this.title = title;
        this.companyImage = companyImage;
        this.price = price;
    }

    public static InsuranceResponse createInsuranceResponse(Insurance insurance) {
        return InsuranceResponse.builder()
                .insuranceId(insurance.getInsuranceId())
                .title(insurance.getTitle())
                .companyImage(insurance.getCompanyImage())
                .price(insurance.getPrice())
                .build();
    }
}
