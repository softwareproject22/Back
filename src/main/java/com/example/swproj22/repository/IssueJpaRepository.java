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

    List<Issue> findByProjectIdAndAssignee(Long projectId, String assignee);

    //@Query("SELECT t FROM Issue i JOIN i.tags t WHERE i.id = :issueId")
    //List<Tag> findTagsByIssueId(@Param("issueId") Long issueId); //주어진 이슈 ID에 대해 해당 이슈에 할당된 모든 태그 조회

    @Query("SELECT i FROM Issue i JOIN i.tags t WHERE i.status = :status AND t.category IN :tagNames")
    List<Issue> findByStatusAndTags(@Param("status") String status, @Param("tagNames") List<Tag> tagNames); //파라미터의 상태와 태그이름이 같은 이슈 조회

    List<Issue> findByStatusIn(List<String> statuses);

    @Query("SELECT FUNCTION('DATE', i.reportedTime), COUNT(i) FROM Issue i GROUP BY FUNCTION('DATE', i.reportedTime)")
    List<Object[]> countIssuesByDay();

    @Query("SELECT FUNCTION('YEAR', i.reportedTime), FUNCTION('MONTH', i.reportedTime), COUNT(i) FROM Issue i GROUP BY FUNCTION('YEAR', i.reportedTime), FUNCTION('MONTH', i.reportedTime)")
    List<Object[]> countIssuesByMonth();

    @Query("SELECT i.status, COUNT(i) FROM Issue i GROUP BY i.status")
    List<Object[]> countIssuesByStatus();

    @Query("SELECT t.category, COUNT(i.id) FROM Issue i JOIN i.tags t GROUP BY t.category")
    List<Object[]> countIssuesByTag();


}