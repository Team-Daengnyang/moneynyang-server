package com.fav.daengnyang.domain.target.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class TargetResponse {
    private Long targetId;
    private String description;
    private int targetAmount;
    private String targetTitle;
    private int currentAmount;
    private Boolean isDone;
    private Boolean isWithdraw;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Long accountId;

    @Builder
    public TargetResponse (Long targetId, String description, int targetAmount, String targetTitle, int currentAmount, Boolean isDone, Boolean isWithdraw, LocalDate startDate, LocalDate endDate, Long accountId) {
        this.targetId = targetId;
        this.description = description;
        this.targetAmount = targetAmount;
        this.targetTitle = targetTitle;
        this.currentAmount = currentAmount;
        this.isDone = isDone;
        this.isWithdraw = isWithdraw;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accountId = accountId;
    }
}
