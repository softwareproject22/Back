package com.example.swproj22.repository;

import com.example.swproj22.domain.Issue;
import com.example.swproj22.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueJpaRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByProjectId(Long projectId);

    List<Issue> findByProjectIdAndStatus(Long projectId, String status);

    List<Issue> findByProjectIdAndReporter(Long projectId, String reporter);

    @Query("SELECT i FROM Issue i WHERE i.projectId = ?1 AND i.assignee = ?2 AND i.status != 'fixed'")
    List<Issue> findByProjectIdAndAssignee(Long projectId, String assignee);

    @Query("SELECT i FROM Issue i JOIN i.tags t WHERE i.status IN :statuses AND t IN :tags")
    List<Issue> findByStatusesAndTags(@Param("statuses") List<String> statuses, @Param("tags") List<Tag> tags);

    List<Issue> findByStatusIn(List<String> statuses);

    @Query("SELECT CAST(i.reportedTime AS date) AS reportDate, COUNT(i) FROM Issue i GROUP BY CAST(i.reportedTime AS date)")
    List<Object[]> countIssuesByDayUsingCast();

    @Query("SELECT FUNCTION('YEAR', i.reportedTime), FUNCTION('MONTH', i.reportedTime), COUNT(i) FROM Issue i GROUP BY FUNCTION('YEAR', i.reportedTime), FUNCTION('MONTH', i.reportedTime)")
    List<Object[]> countIssuesByMonth();

    @Query("SELECT i.status, COUNT(i) FROM Issue i GROUP BY i.status")
    List<Object[]> countIssuesByStatus();

    @Query("SELECT t.category, COUNT(i.id) FROM Issue i JOIN i.tags t GROUP BY t.category")
    List<Object[]> countIssuesByTag();


}