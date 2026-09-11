package com.example.cs_agent.domain.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inquiries")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String message;

    @Column(nullable = false, length = 30)
    private String category;

    @Column(nullable = false, length = 30)
    private String priority;

    @Column(nullable = false, length = 1000)
    private String summary;

    protected Inquiry() {
    }

    public Inquiry(
            String message,
            String category,
            String priority,
            String summary) {
        this.message = message;
        this.category = category;
        this.priority = priority;
        this.summary = summary;
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