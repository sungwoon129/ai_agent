package com.example.cs_agent.service;

import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Service
@ConditionalOnProperty(
        prefix = "app.llm",
        name = "provider",
        havingValue = "mock",
        matchIfMissing = true)
public class MockLlmService implements LlmService {

    @Override
    public LlmClassification classify(String inquiry) {
        String category;
        String priority;

        if (inquiry.contains("결제") || inquiry.contains("카드") || inquiry.contains("환불")) {
            category = "PAYMENT";
            priority = "HIGH";
        } else if (inquiry.contains("배송") || inquiry.contains("택배")) {
            category = "DELIVERY";
            priority = "MEDIUM";
        } else if (inquiry.contains("회원") || inquiry.contains("로그인")) {
            category = "MEMBER";
            priority = "MEDIUM";
        } else {
            category = "ETC";
            priority = "LOW";
        }

        return new LlmClassification(
                category,
                priority,
                inquiry.length() > 30 ? inquiry.substring(0, 30) + "..." : inquiry);
    }
}
