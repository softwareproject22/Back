package com.example.swproj22.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IssueMangerChangeRequest {
    private String nickname;
    private String assignedUserId;
}
