package com.example.swproj22.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueCountRequest {

    private String date;
    private Long issues;

    public IssueCountRequest(String date, Long issues){
        this.date = date;
        this.issues = issues;
    }

}
