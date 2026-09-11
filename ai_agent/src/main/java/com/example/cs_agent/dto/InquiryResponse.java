package com.example.cs_agent.dto;

import com.example.cs_agent.domain.Entity.Inquiry;

public class InquiryResponse {
    private Long id;
    private String message;
    private String category;
    private String priority;
    private String summary;

    public InquiryResponse(Inquiry inquiry) {
        this.id = inquiry.getId();
        this.message = inquiry.getMessage();
        this.category = inquiry.getCategory();
        this.priority = inquiry.getPriority();
        this.summary = inquiry.getSummary();
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getCategory() {
        return category;
    }

    public String getPriority() {
        return priority;
    }

    public String getSummary() {
        return summary;
    }
}
