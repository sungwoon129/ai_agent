package com.example.cs_agent.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cs_agent.domain.Entity.Inquiry;
import com.example.cs_agent.dto.InquiryCreateDto;
import com.example.cs_agent.dto.InquiryResponse;
import com.example.cs_agent.repository.InquiryRepository;

@Service
@Transactional
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final LlmService llmService;

    public InquiryService(
            InquiryRepository inquiryRepository,
            LlmService llmService) {
        this.inquiryRepository = inquiryRepository;
        this.llmService = llmService;
    }

    public InquiryResponse create(
            InquiryCreateDto request) {

        // 1. 고객 문의
        String message = request.getMessage();

        // 2. LLM 분석
        LlmClassification classification = llmService.classify(message);

        // 3. Entity 생성
        Inquiry inquiry = new Inquiry(
                message,
                classification.category(),
                classification.priority(),
                classification.summary());

        // 4. DB 저장
        Inquiry saved = inquiryRepository.save(inquiry);

        // 5. 응답
        return new InquiryResponse(saved);
    }

    @Transactional(readOnly = true)
    public InquiryResponse findById(Long id) {

        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "문의가 존재하지 않습니다."));

        return new InquiryResponse(inquiry);
    }

    @Transactional(readOnly = true)
    public List<InquiryResponse> findAll() {

        return inquiryRepository.findAll()
                .stream()
                .map(InquiryResponse::new)
                .toList();
    }

}
