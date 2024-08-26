package com.fav.daengnyang.domain.target.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long accountId;

    @Builder
    public TargetResponse (Long targetId, String description, int targetAmount, String targetTitle, int currentAmount, Boolean isDone, Long accountId) {
        this.targetId = targetId;
        this.description = description;
        this.targetAmount = targetAmount;
        this.targetTitle = targetTitle;
        this.currentAmount = currentAmount;
        this.isDone = isDone;
        this.accountId = accountId;
    }
}
