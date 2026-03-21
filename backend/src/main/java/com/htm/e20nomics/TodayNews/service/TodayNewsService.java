package com.htm.e20nomics.todaynews.service;

import com.htm.e20nomics.todaynews.domain.TodayNews;
import com.htm.e20nomics.todaynews.domain.TodayNewsTerm;
import com.htm.e20nomics.todaynews.dto.*;
import com.htm.e20nomics.todaynews.repository.TodayNewsRepository;
import com.htm.e20nomics.todaynews.repository.TodayNewsTermRepository;
import com.htm.e20nomics.global.exception.SummaryNotFoundException;
import com.htm.e20nomics.global.exception.TodayNewsNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodayNewsService {


    private final TodayNewsRepository todayNewsRepository;
    private final TodayNewsTermRepository todayNewsTermRepository;


    // 사용자: 오늘의 뉴스 가져오기
    @Transactional(readOnly = true)
    public List<TodayNewsResponse> getPublicTodayNews() {
        List<TodayNews> lists = todayNewsRepository.findAllByIsPublishedTrue();
        return lists.stream().map((list) -> new TodayNewsResponse(list.getId(), list.getSummaryTitle(), list.getSummaryText(), list.getCreatedAt()))
                .toList();
    }

    // 사용자: 오늘의 뉴스 상세페이지 가져오기
    @Transactional(readOnly = true)
    public TodayNewsDetailResponse getPublicTodayNewsDetail(Long todayNewsId) {
        TodayNews todayNews = todayNewsRepository.findById(todayNewsId)
                .orElseThrow(() -> new TodayNewsNotFoundException("오늘의 뉴스를 찾을 수 없습니다."));

        if (!todayNews.getIsPublished()) {
            throw new TodayNewsNotFoundException("오늘의 뉴스를 찾을 수 없습니다.");
        }

        List<TodayNewsTerm> linkedTerms = todayNewsTermRepository.findAllByTodayNewsId(todayNewsId);

        List<TodayNewsTermResponse> terms = linkedTerms.stream().map(link-> new TodayNewsTermResponse(
                link.getAdminTerm().getId(), link.getAdminTerm().getTerm(), link.getAdminTerm().getDefinition())).toList();


        return new TodayNewsDetailResponse(todayNews.getSummaryTitle(), todayNews.getSummaryText(), todayNews.getCreatedAt(), terms);
    }
}
