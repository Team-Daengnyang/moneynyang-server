package com.fav.daengnyang.domain.pet.service.dto.response;

import lombok.*;
import org.hibernate.annotations.Check;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CheckResponse {

    private String responseCode;
    private String responseMessage;

    @Builder
    private CheckResponse(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public static CheckResponse createCheckResponse(String responseMessage) {
        return CheckResponse.builder()
                .responseMessage(responseMessage)
                .build();
    }

    public void  updateCheckResponse(String responseMessage){
        this.responseMessage = responseMessage;
    }
}
