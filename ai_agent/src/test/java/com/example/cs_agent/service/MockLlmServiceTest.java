package com.example.cs_agent.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MockLlmServiceTest {

    private final MockLlmService llmService = new MockLlmService();

    @Test
    void 결제_키워드를_높은_우선순위의_PAYMENT로_분류한다() {
        LlmClassification result = llmService.classify("카드 결제가 두 번 되었습니다.");

        assertThat(result.category()).isEqualTo("PAYMENT");
        assertThat(result.priority()).isEqualTo("HIGH");
        assertThat(result.summary()).isEqualTo("카드 결제가 두 번 되었습니다.");
    }

    @Test
    void 배송_회원_기타_키워드를_각각_분류한다() {
        LlmClassification delivery = llmService.classify("배송이 아직 도착하지 않았어요.");
        LlmClassification member = llmService.classify("로그인이 되지 않습니다.");
        LlmClassification etc = llmService.classify("상품 색상이 궁금합니다.");

        assertThat(delivery.category()).isEqualTo("DELIVERY");
        assertThat(delivery.priority()).isEqualTo("MEDIUM");
        assertThat(member.category()).isEqualTo("MEMBER");
        assertThat(member.priority()).isEqualTo("MEDIUM");
        assertThat(etc.category()).isEqualTo("ETC");
        assertThat(etc.priority()).isEqualTo("LOW");
    }

    @Test
    void 긴_문의는_앞의_30자만_요약하고_말줄임표를_붙인다() {
        String message = "123456789012345678901234567890추가 내용";

        LlmClassification result = llmService.classify(message);

        assertThat(result.summary()).isEqualTo("123456789012345678901234567890...");
    }
}
