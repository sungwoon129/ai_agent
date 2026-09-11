package com.example.cs_agent.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "app.llm.provider=openai",
        "spring.ai.model.chat=openai"
})
class OpenAiLlmServiceConfigurationTest {

    @Autowired
    private LlmService llmService;

    @Test
    void openai_설정에서는_OpenAiLlmService만_등록된다() {
        assertThat(llmService).isInstanceOf(OpenAiLlmService.class);
    }
}
