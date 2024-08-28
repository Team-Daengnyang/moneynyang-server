package com.fav.daengnyang.domain.cashwalk.service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CashwalkRequest {
    private String content;
    private LocalDate createdAt;

    @Builder
    public CashwalkRequest(String content, LocalDate createdAt) {
        this.content = content;
        this.createdAt = createdAt;
    }
}
