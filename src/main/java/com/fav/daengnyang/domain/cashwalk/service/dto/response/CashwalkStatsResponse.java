package com.fav.daengnyang.domain.cashwalk.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CashwalkStatsResponse {
    private int totalDiaries; // 총 일지 작성 횟수
    private int achievementRate; // 일지 작성 달성률 (%)

    @Builder
    public CashwalkStatsResponse(int totalDiaries, int achievementRate) {
        this.totalDiaries = totalDiaries;
        this.achievementRate = achievementRate;
    }
}
