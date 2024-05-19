package com.example.swproj22.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueStatusChangeRequest {
    private String nickname;

    @NotBlank(message = "Status is mandatory")
    private String status;
}