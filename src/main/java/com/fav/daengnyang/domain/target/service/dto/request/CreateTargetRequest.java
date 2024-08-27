package com.fav.daengnyang.domain.target.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CreateTargetRequest {

    @NotNull(message = "목표 제목을 설정해 주세요.")
    private String targetTitle;

    private String description;

    @NotNull(message = "목표 설정 시 목표 금액을 필수로 정해야 합니다.")
    private int targetAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Builder
    public CreateTargetRequest(String targetTitle, String description, int targetAmount, LocalDate startDate, LocalDate endDate) {
        this.targetTitle = targetTitle;
        this.description = description;
        this.targetAmount = targetAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
