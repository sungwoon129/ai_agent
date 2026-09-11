package com.example.cs_agent.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.cs_agent.domain.Entity.Inquiry;
import com.example.cs_agent.dto.InquiryCreateDto;
import com.example.cs_agent.dto.InquiryResponse;
import com.example.cs_agent.repository.InquiryRepository;

@ExtendWith(MockitoExtension.class)
class InquiryServiceTest {

    @Mock
    private InquiryRepository inquiryRepository;

    @Mock
    private LlmService llmService;

    @Captor
    private ArgumentCaptor<Inquiry> inquiryCaptor;

    private InquiryService inquiryService;

    @BeforeEach
    void setUp() {
        inquiryService = new InquiryService(inquiryRepository, llmService);
    }

    @Test
    void 문의를_LLM으로_분석해_저장하고_응답으로_반환한다() {
        InquiryCreateDto request = request("결제가 되지 않습니다.");
        given(llmService.classify(request.getMessage()))
                .willReturn(new LlmClassification("PAYMENT", "HIGH", "결제 오류"));
        given(inquiryRepository.save(any(Inquiry.class))).willAnswer(invocation -> invocation.getArgument(0));

        InquiryResponse response = inquiryService.create(request);

        then(llmService).should().classify("결제가 되지 않습니다.");
        then(inquiryRepository).should().save(inquiryCaptor.capture());
        assertThat(inquiryCaptor.getValue().getMessage()).isEqualTo("결제가 되지 않습니다.");
        assertThat(inquiryCaptor.getValue().getCategory()).isEqualTo("PAYMENT");
        assertThat(inquiryCaptor.getValue().getPriority()).isEqualTo("HIGH");
        assertThat(inquiryCaptor.getValue().getSummary()).isEqualTo("결제 오류");
        assertThat(response.getMessage()).isEqualTo("결제가 되지 않습니다.");
        assertThat(response.getCategory()).isEqualTo("PAYMENT");
    }

    @Test
    void 식별자로_문의를_조회한다() {
        Inquiry inquiry = new Inquiry("배송 조회", "DELIVERY", "MEDIUM", "배송 조회");
        given(inquiryRepository.findById(10L)).willReturn(Optional.of(inquiry));

        InquiryResponse response = inquiryService.findById(10L);

        assertThat(response.getMessage()).isEqualTo("배송 조회");
        assertThat(response.getCategory()).isEqualTo("DELIVERY");
        then(inquiryRepository).should().findById(10L);
    }

    @Test
    void 없는_문의_조회는_명확한_예외를_던진다() {
        given(inquiryRepository.findById(404L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> inquiryService.findById(404L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("문의가 존재하지 않습니다.");
    }

    @Test
    void 모든_문의를_저장된_순서대로_응답으로_변환한다() {
        given(inquiryRepository.findAll()).willReturn(List.of(
                new Inquiry("환불 문의", "PAYMENT", "HIGH", "환불 문의"),
                new Inquiry("회원 정보 변경", "MEMBER", "MEDIUM", "회원 정보 변경")));

        List<InquiryResponse> responses = inquiryService.findAll();

        assertThat(responses)
                .extracting(InquiryResponse::getMessage)
                .containsExactly("환불 문의", "회원 정보 변경");
        assertThat(responses)
                .extracting(InquiryResponse::getCategory)
                .containsExactly("PAYMENT", "MEMBER");
        then(inquiryRepository).should().findAll();
    }

    private InquiryCreateDto request(String message) {
        InquiryCreateDto request = new InquiryCreateDto();
        request.setMessage(message);
        return request;
    }
}
