package com.fav.daengnyang.domain.memberDetail.service.dto.request;

import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class AnalyzeMonthlyRequest {
    @NotBlank
    private LocalDate date;
}
