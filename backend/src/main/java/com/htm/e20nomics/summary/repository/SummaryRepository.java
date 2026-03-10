package com.htm.e20nomics.summary.repository;

import com.htm.e20nomics.summary.domain.CreatedBy;
import com.htm.e20nomics.summary.domain.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SummaryRepository extends JpaRepository<Summary, Long> {
    List<Summary> findAllByAuthorId(Long userId);
    List<Summary> findAllByCreatedBy(CreatedBy createdBy);
}
