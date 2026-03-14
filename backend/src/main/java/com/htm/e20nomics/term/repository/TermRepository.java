package com.htm.e20nomics.term.repository;

import com.htm.e20nomics.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermRepository extends JpaRepository<Term, Long> {
    List<Term> findAllByAuthorId(Long userId);
}
