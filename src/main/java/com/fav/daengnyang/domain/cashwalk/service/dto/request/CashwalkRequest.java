package com.fav.daengnyang.domain.cashwalk.service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CashwalkRequest {
    private String content;
    private LocalDate createdAt;
    private MultipartFile image;

    @Builder
    public CashwalkRequest(String content, LocalDate createdAt, MultipartFile image) {
        this.content = content;
        this.createdAt = createdAt;
        this.image = image;
    }
}
