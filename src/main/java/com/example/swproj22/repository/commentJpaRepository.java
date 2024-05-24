package com.example.swproj22.repository;

import com.example.swproj22.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface commentJpaRepository extends JpaRepository<Comment, Long> {
}
