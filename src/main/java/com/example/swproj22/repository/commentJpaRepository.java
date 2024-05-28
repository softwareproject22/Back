package com.example.swproj22.repository;

import com.example.swproj22.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface commentJpaRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByIssueId(Long issueId);
}
