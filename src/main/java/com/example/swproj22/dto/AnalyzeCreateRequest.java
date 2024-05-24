package com.example.swproj22.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AnalyzeCreateRequest {

    private Long issueId;
    private LocalDate analysisDate;
    private int issueCount;
    private String details;
}
