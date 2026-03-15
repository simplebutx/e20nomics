package com.htm.e20nomics.TodayNews.repository;

import com.htm.e20nomics.TodayNews.domain.TodayNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodayNewsRepository extends JpaRepository<TodayNews, Long> {
    List<TodayNews> findAllByIsPublishedTrue();
    List<TodayNews> findAllByOrderByCreatedAtDesc();
    List<TodayNews> findAllByIsPublishedTrueOrderByCreatedAtDesc();
}
