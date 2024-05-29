package com.example.swproj22.controller;

import com.example.swproj22.domain.Issue;
import com.example.swproj22.domain.Tag;
import com.example.swproj22.repository.IssueJpaRepository;
import com.example.swproj22.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    private final RecommendService recommendService;
    private final IssueJpaRepository issueJpaRepository;

    @Autowired
    public RecommendController(RecommendService recommendService, IssueJpaRepository issueJpaRepository) {
        this.recommendService = recommendService;
        this.issueJpaRepository = issueJpaRepository;
    }

    @GetMapping("/assigneesForIssue/{issueId}")
    public ResponseEntity<List<String>> getRecommendedAssigneesForIssue(@PathVariable Long issueId) {
        Issue issue = issueJpaRepository.findById(issueId).orElseThrow(() -> new RuntimeException("Issue not found"));
        List<Tag> tags = issue.getTags(); // 이슈에 연결된 태그 목록
        List<String> recommendedAssignees = recommendService.recommendAssigneesByTag(tags);
        return ResponseEntity.ok(recommendedAssignees);
    }
}
