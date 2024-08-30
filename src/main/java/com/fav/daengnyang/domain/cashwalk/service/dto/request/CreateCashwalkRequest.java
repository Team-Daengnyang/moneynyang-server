package com.fav.daengnyang.domain.cashwalk.service.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CreateCashwalkRequest {
    private String content;
    private LocalDate createdAt;
    private MultipartFile image;

    @Builder
    public CreateCashwalkRequest(String content, LocalDate createdAt, MultipartFile image) {
        this.content = content;
        this.createdAt = createdAt;
        this.image = image;
    }
}
