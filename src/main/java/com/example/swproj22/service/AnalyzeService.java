package com.example.swproj22.service;

import com.example.swproj22.domain.Issue;
import com.example.swproj22.domain.common.Analyze;
import com.example.swproj22.repository.AnalyzeRepository;
import com.example.swproj22.repository.IssueJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;

@Service
public class AnalyzeService {

    private final AnalyzeRepository analyzeRepository;
    private final IssueJpaRepository issueJpaRepository;

    @Autowired
    public AnalyzeService(AnalyzeRepository analyzeRepository, IssueJpaRepository issueJpaRepository){
        this.analyzeRepository = analyzeRepository;
        this.issueJpaRepository = issueJpaRepository;
    }

    public Analyze createAndSaveAnalysis(Long issueId, LocalDate analysisDate, int issueCount, String details){
        Issue issue = issueJpaRepository.findById(issueId).orElseThrow(() -> new RuntimeException("Issue not found with id : " + issueId));
        Analyze analyze = new Analyze();
        analyze.setIssue(issue);
        analyze.setAnalysisDate(analysisDate);
        analyze.setIssueCount(issueCount);
        analyze.setDetails(details);

        return analyzeRepository.save(analyze);
    }

    public List<Analyze> findAllAnalyses(){
        return analyzeRepository.findAll();
    }
}
