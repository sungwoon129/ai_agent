package com.example.cs_agent.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        prefix = "app.llm",
        name = "provider",
        havingValue = "openai")
public class OpenAiLlmService implements LlmService {

    private static final String SYSTEM_PROMPT = """
            당신은 고객 문의를 분류하는 도우미입니다.
            문의를 분석해 category, priority, summary 필드를 가진 JSON만 반환하세요.
            category는 PAYMENT, DELIVERY, MEMBER, ETC 중 하나여야 합니다.
            priority는 HIGH, MEDIUM, LOW 중 하나여야 합니다.
            summary는 한국어로 100자 이내여야 합니다.
            Markdown 코드 블록이나 설명은 절대 포함하지 마세요.
            """;

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenAiLlmService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public LlmClassification classify(String inquiry) {
        String response = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(inquiry)
                .call()
                .content();

        try {
            return objectMapper.readValue(response, LlmClassification.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("OpenAI 응답을 분류 결과로 해석할 수 없습니다.", exception);
        }
    }
}
