package com.fav.daengnyang.domain.account.service.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class ImageUpdateRequest {
    private MultipartFile accoutImage;
}
