package com.fav.daengnyang.domain.target.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AchievedTargetInfoResponse {
    private String targetTitle;
    private int targetAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate completedDate;

    @Builder
    public AchievedTargetInfoResponse(String targetTitle, int targetAmount, LocalDate completedDate) {
        this.targetTitle = targetTitle;
        this.targetAmount = targetAmount;
        this.completedDate = completedDate;
    }
}
