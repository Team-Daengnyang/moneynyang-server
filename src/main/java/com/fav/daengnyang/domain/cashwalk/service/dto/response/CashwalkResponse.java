package com.fav.daengnyang.domain.cashwalk.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class CashwalkResponse {
    private Long cashwalkId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String diaryContent;
    private String imageUrl;

    @Builder
    public CashwalkResponse(Long cashwalkId, LocalDate date, String diaryContent, String imageUrl) {
        this.cashwalkId = cashwalkId;
        this.date = date;
        this.diaryContent = diaryContent;
        this.imageUrl = imageUrl;
    }
}
