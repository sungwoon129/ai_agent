package com.example.cs_agent;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cs_agent.service.LlmService;
import com.example.cs_agent.service.MockLlmService;

@SpringBootTest
class AiAgentApplicationTests {

	@Autowired
	private LlmService llmService;

	@Test
	void contextLoads() {
		assertThat(llmService).isInstanceOf(MockLlmService.class);
	}

}
