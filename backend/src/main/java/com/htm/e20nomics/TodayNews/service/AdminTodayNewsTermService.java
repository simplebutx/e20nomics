package com.htm.e20nomics.TodayNews.service;


import com.htm.e20nomics.TodayNews.domain.TodayNews;
import com.htm.e20nomics.TodayNews.repository.TodayNewsRepository;
import com.htm.e20nomics.term.domain.AdminTerm;
import com.htm.e20nomics.TodayNews.domain.TodayNewsTerm;
import com.htm.e20nomics.term.dto.AdminTermResponse;
import com.htm.e20nomics.term.repository.AdminTermRepository;
import com.htm.e20nomics.TodayNews.repository.TodayNewsTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTodayNewsTermService {

    private final AdminTermRepository adminTermRepository;
    private final TodayNewsRepository todayNewsRepository;
    private final TodayNewsTermRepository todayNewsTermRepository;

    public List<AdminTermResponse> getLinkedTerms(Long todayNewsId) {
        TodayNews todayNews = todayNewsRepository.findById(todayNewsId)
                .orElseThrow(() -> new RuntimeException("오늘의 뉴스를 찾을 수 없습니다."));

        return todayNewsTermRepository.findAllByTodayNewsId(todayNews.getId()).stream()
                .map(todayNewsTerm -> new AdminTermResponse(todayNewsTerm.getAdminTerm()))
                .toList();
    }

    @Transactional
    public void linkTerm(Long todayNewsId, Long termId) {
        TodayNews todayNews = todayNewsRepository.findById(todayNewsId)
                .orElseThrow(() -> new RuntimeException("오늘의 뉴스를 찾을 수 없습니다."));

        AdminTerm adminTerm = adminTermRepository.findById(termId)
                .orElseThrow(() -> new RuntimeException("단어를 찾을 수 없습니다."));

        if (todayNewsTermRepository.existsByTodayNewsIdAndAdminTermId(todayNewsId, termId)) {  // 중복검사
            throw new RuntimeException("이미 연결된 단어입니다.");
        }

        todayNewsTermRepository.save(new TodayNewsTerm(todayNews, adminTerm));
    }

    @Transactional
    public void unlinkTerm(Long todayNewsId, Long termId) {
        TodayNewsTerm todayNewsTerm = todayNewsTermRepository
                .findByTodayNewsIdAndAdminTermId(todayNewsId, termId)
                .orElseThrow(() -> new RuntimeException("연결된 단어를 찾을 수 없습니다."));

        todayNewsTermRepository.delete(todayNewsTerm);
    }
}
