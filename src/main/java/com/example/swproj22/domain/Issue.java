package com.example.swproj22.domain;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
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

    //@ElementCollection
    //private List<String> tags;

    private LocalDateTime reportedTime;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "tagging",
            joinColumns = @JoinColumn(name = "issue_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();
}
