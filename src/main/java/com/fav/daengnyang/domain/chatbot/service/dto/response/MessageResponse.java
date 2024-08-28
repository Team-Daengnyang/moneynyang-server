package com.fav.daengnyang.domain.chatbot.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MessageResponse {
    private String chat;

    @Builder
    public MessageResponse(String chat) {
        this.chat = chat;
    }
}
