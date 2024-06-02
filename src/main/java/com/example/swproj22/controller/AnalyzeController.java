package com.example.swproj22.controller;

import com.example.swproj22.domain.common.Analyze;
import com.example.swproj22.dto.AnalyzeCreateRequest;
import com.example.swproj22.dto.IssueCountRequest;
import com.example.swproj22.service.AnalyzeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/analyze")
public class AnalyzeController {

    private  final AnalyzeService analyzeService;

    @Autowired
    public AnalyzeController(AnalyzeService analyzeService){
        this.analyzeService = analyzeService;
    }

    @GetMapping("/daily")
    public ResponseEntity<Map<Integer, Long>> getDailyIssueCounts() {
        Map<Integer, Long> dailyCounts = analyzeService.countIssuesByDay();
        return ResponseEntity.ok(dailyCounts);
    }

    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Long>> getMonthlyIssueCounts() {
        Map<String, Long> monthlyCounts = analyzeService.countIssuesByMonth();
        return ResponseEntity.ok(monthlyCounts);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Long>> getStatusIssueCounts() {
        Map<String, Long> statusCounts = analyzeService.countIssuesByStatus();
        return ResponseEntity.ok(statusCounts);
    }

    @GetMapping("/tags")
    public ResponseEntity<Map<String, Long>> getTagIssueCounts() {
        Map<String, Long> tagCounts = analyzeService.countIssuesByTag();
        return ResponseEntity.ok(tagCounts);
    }
}