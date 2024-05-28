package com.example.swproj22.repository;

import com.example.swproj22.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueJpaRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByProjectId(Long projectId);

    List<Issue> findByProjectIdAndStatus(Long projectId, String status);

    List<Issue> findByProjectIdAndReporter(Long projectId, String reporter);

    List<Issue> findByProjectIdAndAssignee(Long projectId, String assignee);

    @Query("SELECT FUNCTION('DATE', i.reportedTime), COUNT(i) FROM Issue i GROUP BY FUNCTION('DATE', i.reportedTime)")
    List<Object[]> countIssuesByDay();

    @Query("SELECT FUNCTION('YEAR', i.reportedTime), FUNCTION('MONTH', i.reportedTime), COUNT(i) FROM Issue i GROUP BY FUNCTION('YEAR', i.reportedTime), FUNCTION('MONTH', i.reportedTime)")
    List<Object[]> countIssuesByMonth();

    @Query("SELECT i.status, COUNT(i) FROM Issue i GROUP BY i.status")
    List<Object[]> countIssuesByStatus();

}