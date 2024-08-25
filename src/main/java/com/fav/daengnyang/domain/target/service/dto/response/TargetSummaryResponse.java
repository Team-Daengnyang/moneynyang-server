package com.fav.daengnyang.domain.target.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class TargetSummaryResponse {
    private double averageDepositsPerTarget;
    private double averageAmountPerDeposit;
    private int achievedTargetsCount;
    private List<AchievedTargetInfoResponse> achievedTargets;

    @Builder
    public TargetSummaryResponse(double averageDepositsPerTarget, double averageAmountPerDeposit, int achievedTargetsCount, List<AchievedTargetInfoResponse> achievedTargets) {
        this.averageDepositsPerTarget = averageDepositsPerTarget;
        this.averageAmountPerDeposit = averageAmountPerDeposit;
        this.achievedTargetsCount = achievedTargetsCount;
        this.achievedTargets = achievedTargets;
    }
}
