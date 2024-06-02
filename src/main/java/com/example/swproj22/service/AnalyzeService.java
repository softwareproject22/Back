package com.example.swproj22.service;

import com.example.swproj22.dto.IssueCountRequest;
import com.example.swproj22.repository.IssueJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class AnalyzeService {

    private final IssueJpaRepository issueJpaRepository;

    @Autowired
    public AnalyzeService(IssueJpaRepository issueJpaRepository){
        this.issueJpaRepository = issueJpaRepository;
    }

    public Map<Integer, Long> countIssuesByDay() {
        List<Object[]> results = issueJpaRepository.countIssuesByDayUsingCast();
        Map<Integer, Long> dailyCounts = new HashMap<>();
        for (Object[] result : results) {
            Integer day = Integer.parseInt(result[0].toString().split("-")[2]);
            dailyCounts.put(day, (Long) result[1]);
        }
        return dailyCounts;
    }

    public Map<String, Long> countIssuesByMonth() {
        List<Object[]> results = issueJpaRepository.countIssuesByMonth();
        Map<String, Long> monthlyCounts = new HashMap<>();
        for (Object[] result : results) {
            String yearMonth = result[0] + "-" + String.format("%02d", result[1]);
            monthlyCounts.put(yearMonth, (Long) result[2]);
        }
        return monthlyCounts;
    }

    public Map<String, Long> countIssuesByStatus() {
        List<Object[]> results = issueJpaRepository.countIssuesByStatus();
        Map<String, Long> statusCounts = new HashMap<>();
        for (Object[] result : results) {
            statusCounts.put((String) result[0], (Long) result[1]);
        }
        return statusCounts;
    }

    public Map<String, Long> countIssuesByTag() {
        List<Object[]> results = issueJpaRepository.countIssuesByTag();
        Map<String, Long> tagCounts = new HashMap<>();
        for (Object[] result : results) {
            tagCounts.put((String) result[0], (Long) result[1]);
        }
        return tagCounts;
    }

}
