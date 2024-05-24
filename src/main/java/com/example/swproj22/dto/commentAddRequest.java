package com.example.swproj22.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class commentAddRequest {

    @NotBlank
    @Size(max = 300, message = "Comment can have up to 300 characters")
    private String content;

    private Long issueId;

    @NotBlank
    private String sender;
}
