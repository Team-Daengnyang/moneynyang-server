package com.fav.daengnyang.domain.target.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class TargetDetailResponse {
    private Long detailId;
    private int amount;
    private LocalDate createdDate;

    @Builder
    public TargetDetailResponse(Long detailId, int amount, LocalDate createdDate) {
        this.detailId = detailId;
        this.amount = amount;
        this.createdDate = createdDate;
    }
}
