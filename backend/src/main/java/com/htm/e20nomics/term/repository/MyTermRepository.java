package com.htm.e20nomics.term.repository;

import com.htm.e20nomics.term.domain.MyTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyTermRepository extends JpaRepository<MyTerm, Long> {
    List<MyTerm> findAllByAuthorId(Long userId);
}
