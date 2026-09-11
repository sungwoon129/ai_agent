package com.example.cs_agent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cs_agent.domain.Entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

}
