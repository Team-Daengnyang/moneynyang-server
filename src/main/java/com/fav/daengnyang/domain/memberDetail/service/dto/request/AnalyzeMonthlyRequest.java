package com.fav.daengnyang.domain.memberDetail.service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.YearMonth;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class AnalyzeMonthlyRequest {
    @NotNull
    private YearMonth yearMonth;
}
