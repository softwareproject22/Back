package com.example.swproj22.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
public class IssueEditCodeRequest {

    private String code;
    public IssueEditCodeRequest(String code) {
        this.code = code;
    }
}
