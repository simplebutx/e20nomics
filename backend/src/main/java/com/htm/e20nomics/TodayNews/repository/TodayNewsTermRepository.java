package com.htm.e20nomics.TodayNews.repository;

import com.htm.e20nomics.TodayNews.domain.TodayNewsTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodayNewsTermRepository extends JpaRepository<TodayNewsTerm, Long> {

    List<TodayNewsTerm> findAllByTodayNewsId(Long todayNewsId);

    boolean existsByTodayNewsIdAndAdminTermId(Long todayNewsId, Long adminTermId);

    Optional<TodayNewsTerm> findByTodayNewsIdAndAdminTermId(Long todayNewsId, Long adminTermId);

    void deleteByAdminTermId(Long adminTermId);
}