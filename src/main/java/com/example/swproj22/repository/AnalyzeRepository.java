package com.example.swproj22.repository;

import com.example.swproj22.domain.common.Analyze;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyzeRepository extends JpaRepository<Analyze, Long> {
}
