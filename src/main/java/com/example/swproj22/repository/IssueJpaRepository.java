package com.example.swproj22.repository;

import com.example.swproj22.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueJpaRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByProjectId(Long projectId);

    List<Issue> findByProjectIdAndStatus(Long projectId, String status);


}