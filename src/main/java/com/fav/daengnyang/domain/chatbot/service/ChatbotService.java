package com.fav.daengnyang.domain.chatbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fav.daengnyang.domain.chatbot.entity.Chatbot;
import com.fav.daengnyang.domain.chatbot.repository.ChatbotRepository;
import com.fav.daengnyang.domain.chatbot.service.dto.request.CreateMessageRequest;
import com.fav.daengnyang.domain.chatbot.service.dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatbotService {

    private final ChatbotRepository chatbotRepository;

    // GPT API 키
    @Value("${gpt-key}")
    private String gptKey;

    // assistant ID
    @Value(("${assistant-id}"))
    private String assistantId;

    // 유저의 메시지에 대한 응답 받기
    public MessageResponse getMessage(Long memberId, CreateMessageRequest messageRequest) throws InterruptedException {
        String threadId = getThreadId(memberId);
        sendMessage(threadId, messageRequest.getChat());
        runMessages(threadId, assistantId);
        Thread.sleep(1500);
        List<String> messages = getMessageTextValues(threadId);
        MessageResponse response = MessageResponse.builder()
                .chat(messages.get(0))
                .build();
        return response;
    }

    // 세션 생성하면서 첫 메시지 보내는 메소드
    public void createSession(Long memberId) {
        chatbotRepository.deleteById(memberId);
        getThreadId(memberId);
    }

    // 지금 스레드(세션) id 가져오기
    private String getThreadId(Long memberId) {
        try {
            Chatbot chatbot = chatbotRepository.findById(memberId).orElseThrow(Exception::new);
            return chatbot.getThreadId();
        } catch (Exception e){
            String thread = createThreadId();
            Chatbot chatbot = new Chatbot();
            chatbot.setThreadId(thread);
            chatbot.setMemberId(memberId);
            chatbotRepository.save(chatbot);
            return thread;
        }
    }

    // 새로운 스레드(세션 만들기)
    private String createThreadId() {
        String URL = "https://api.openai.com/v1/threads";
        try {
            // HTTP 요청 설정
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + gptKey);
            headers.set("OpenAI-Beta", "assistants=v2");
            HttpEntity<String> entity = new HttpEntity<>("", headers);

            // API 호출
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
            String jsonResponse = response.getBody();

            // JSON 응답에서 id 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            return rootNode.path("id").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 메시지 전송하기
    private void sendMessage(String threadId, String messageContent) {
        String BASE_URL = "https://api.openai.com/v1/threads/";
        try {
            // HTTP 요청 설정
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + gptKey);
            headers.set("OpenAI-Beta", "assistants=v2");

            // 요청 바디 설정
            String requestBody = String.format(
                    "{\"role\": \"user\", \"content\": \"%s\"}",
                    messageContent
            );
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // API 호출
            String url = BASE_URL + threadId + "/messages";
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    // 메시지를 GPT에서 동작시킴
    private void runMessages(String threadId, String assistantId) {
        String BASE_URL = "https://api.openai.com/v1/threads/";
        try {
            // HTTP 요청 설정
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + gptKey);
            headers.set("Content-Type", "application/json");
            headers.set("OpenAI-Beta", "assistants=v2");

            // 요청 바디 설정
            String requestBody = String.format(
                    "{\"assistant_id\": \"%s\"}",
                    assistantId
            );
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // API 호출
            String url = BASE_URL + threadId + "/runs";
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    // Message의 Text를 받아서 반환하는 메소드
    private List<String> getMessageTextValues(String threadId) {
        String BASE_URL = "https://api.openai.com/v1/threads/";
        try {
            // HTTP 요청 설정
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + gptKey);
            headers.set("OpenAI-Beta", "assistants=v2");

            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            // API 호출
            String url = BASE_URL + threadId + "/messages";
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // 응답 처리
            String jsonResponse = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            List<String> textValues = new ArrayList<>();

            // 메시지에서 text value 추출
            for (JsonNode messageNode : rootNode.path("data")) {
                for (JsonNode contentNode : messageNode.path("content")) {
                    if (contentNode.path("type").asText().equals("text")) {
                        String textValue = contentNode.path("text").path("value").asText();
                        textValues.add(textValue);
                    }
                }
            }

            return textValues;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
