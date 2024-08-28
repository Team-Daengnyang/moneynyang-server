package com.fav.daengnyang.domain.chatbot.controller;

import com.fav.daengnyang.domain.chatbot.service.ChatbotService;
import com.fav.daengnyang.domain.chatbot.service.dto.request.CreateMessageRequest;
import com.fav.daengnyang.domain.chatbot.service.dto.response.MessageResponse;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.dto.response.ExceptionResponse;
import com.fav.daengnyang.global.web.dto.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/chatbots")
@RestController
public class ChatbotController {

    private final ChatbotService chatbotService;

    // 챗봇에게 메시지 보내고 응답 받기
    @PostMapping
    public SuccessResponse<?> createMessage(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @Valid @RequestBody CreateMessageRequest messageRequest
    ) throws InterruptedException {
        MessageResponse response = chatbotService.getMessage(memberPrincipal.getMemberId(), messageRequest);
        return SuccessResponse.ok(response);
    }

    // 챗봇에게 처음 메시지 보내기(새로운 세션 만들기)
    @PostMapping("/session")
    public SuccessResponse<?> createSession(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ){
        chatbotService.createSession(memberPrincipal.getMemberId());
        return SuccessResponse.created("세션 생성에 성공했습니다.");
    }


}
