package com.fav.daengnyang.domain.chatbot.service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMessageRequest {

    @NotNull(message = "챗봇에게 보낼 메시지를 입력해 주세요.")
    private String chat;
}
