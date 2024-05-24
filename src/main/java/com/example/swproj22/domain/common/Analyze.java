package com.example.swproj22.domain.common;

import com.example.swproj22.domain.Issue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Analyze {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Column(name = "analysis_date")
    private LocalDate analysisDate;

    @Column(name = "issue_count")
    private int issueCount;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;
}
