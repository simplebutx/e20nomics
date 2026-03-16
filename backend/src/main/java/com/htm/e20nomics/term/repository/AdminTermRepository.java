package com.htm.e20nomics.term.repository;

import com.htm.e20nomics.term.domain.AdminTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminTermRepository extends JpaRepository<AdminTerm, Long> {
    boolean existsByTerm(String term);
    Optional<AdminTerm> findByTerm(String term);
}
