package com.example.cs_agent.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.cs_agent.dto.InquiryCreateDto;
import com.example.cs_agent.dto.InquiryResponse;
import com.example.cs_agent.service.InquiryService;

@RestController
@RequestMapping("/api/inquiries")
public class InquiryController {
    private final InquiryService inquiryService;

    public InquiryController(
            InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InquiryResponse create(
            @RequestBody InquiryCreateDto request) {

        return inquiryService.create(request);
    }

    @GetMapping("/{id}")
    public InquiryResponse findById(
            @PathVariable Long id) {

        return inquiryService.findById(id);
    }

    @GetMapping
    public List<InquiryResponse> findAll() {

        return inquiryService.findAll();
    }

}
