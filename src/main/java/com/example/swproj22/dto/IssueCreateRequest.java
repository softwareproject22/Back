package com.example.swproj22.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class IssueCreateRequest {
    @NotBlank
    private Long projectId;

    @NotBlank
    private String title;

    @Size(max = 1000, message = "Description can have up to 1000 characters")
    private String description;

    @NotBlank
    private String code;

    @NotNull
    private String reporter;

    private List<Long> tags;


}
