package com.example.swproj22.controller;

import com.example.swproj22.domain.common.Analyze;
import com.example.swproj22.dto.AnalyzeCreateRequest;
import com.example.swproj22.service.AnalyzeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analyze")
public class AnalyzeController {

    private  final AnalyzeService analyzeService;

    @Autowired
    public AnalyzeController(AnalyzeService analyzeService){
        this.analyzeService = analyzeService;
    }

    @GetMapping
    public ResponseEntity<List<Analyze>> getAllAnalyses(){
        List<Analyze> analyses = analyzeService.findAllAnalyses();
        return ResponseEntity.ok(analyses);
    }

    @PostMapping
    public ResponseEntity<Analyze> createAnalyze(@RequestBody AnalyzeCreateRequest request){
        Analyze savedAnalyze = analyzeService.createAndSaveAnalysis(
                request.getIssueId(),
                request.getAnalysisDate(),
                request.getIssueCount(),
                request.getDetails()
        );
        return ResponseEntity.ok(savedAnalyze);
    }
}
