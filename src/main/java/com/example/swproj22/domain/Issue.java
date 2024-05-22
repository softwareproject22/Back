package com.example.swproj22.domain;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    private String title;

    @Column(length = 1000)
    private String description;

    private String code;

    private String status;

    private String reporter;
    private String assignee;
    private String fixer;

    private String priority;

    @ElementCollection
    private List<String> tags;

    private LocalDateTime reportedTime;


}
