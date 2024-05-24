package com.example.swproj22.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagAddToIssueRequest {
    private Long issueId;
    private Long tagId;
}
