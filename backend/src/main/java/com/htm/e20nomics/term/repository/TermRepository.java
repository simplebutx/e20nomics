package com.htm.e20nomics.term.repository;

import com.htm.e20nomics.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {
}
