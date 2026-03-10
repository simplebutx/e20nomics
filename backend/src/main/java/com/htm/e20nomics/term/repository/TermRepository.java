package com.htm.e20nomics.term.repository;

import com.htm.e20nomics.term.domain.Term;
import com.htm.e20nomics.term.domain.TermScope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermRepository extends JpaRepository<Term, Long> {
    List<Term> findAllByAuthorId(Long userId);
    List<Term> findAllByTermScope(TermScope termScope);
    List<Term> findAllByTermScopeOrAuthorId(TermScope termScope, Long userId);
}
