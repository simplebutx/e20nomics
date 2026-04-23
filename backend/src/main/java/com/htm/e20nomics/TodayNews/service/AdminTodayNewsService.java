package com.htm.e20nomics.todaynews.service;

import com.htm.e20nomics.global.exception.TodayNewsNotFoundException;
import com.htm.e20nomics.todaynews.ai.TodayNewsAiClient;
import com.htm.e20nomics.todaynews.domain.TodayNews;
import com.htm.e20nomics.todaynews.dto.AdminImageGenerateResponse;
import com.htm.e20nomics.todaynews.dto.AdminTodayNewsCreateRequest;
import com.htm.e20nomics.todaynews.dto.AdminTodayNewsDetailResponse;
import com.htm.e20nomics.todaynews.dto.AdminTodayNewsGenerateResponse;
import com.htm.e20nomics.todaynews.dto.AdminTodayNewsResponse;
import com.htm.e20nomics.todaynews.dto.AdminTodayNewsUpdateRequest;
import com.htm.e20nomics.todaynews.repository.TodayNewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTodayNewsService {

    private final TodayNewsAiClient todayNewsAiClient;
    private final TodayNewsRepository todayNewsRepository;

    // 관리자: 오늘의 뉴스 생성하기
    @Transactional
    public AdminTodayNewsGenerateResponse summarize(String text) {
        if (!StringUtils.hasText(text)) {    // null, 공백, 길이를 모두 한번에 체크
            return new AdminTodayNewsGenerateResponse("", "요약할 텍스트가 비어있습니다.", false);
        }

        try {
            return todayNewsAiClient.generateSummary(text);
        } catch (Exception e) {
            return new AdminTodayNewsGenerateResponse("", "제목/요약에 실패했습니다.", false);
        }
    }

    // 관리자: 오늘의 뉴스 저장하기
    @Transactional
    public void saveTodayNews(AdminTodayNewsCreateRequest dto) {
        TodayNews todayNews = new TodayNews(dto.getOriginalText(), dto.getSummaryTitle(), dto.getSummaryText());
        todayNewsRepository.save(todayNews);
    }

    // 관리자: 오늘의 뉴스 목록 가져오기
    @Transactional
    public List<AdminTodayNewsResponse> getTodayNewsList() {
        List<TodayNews> lists = todayNewsRepository.findAll();
        return lists.stream().map((list) -> new AdminTodayNewsResponse(list.getId(), list.getSummaryTitle(), list.getSummaryText(), list.getIsPublished(), list.getCreatedAt()))
                .toList();
    }

    // 관리자: 오늘의 뉴스 상세페이지 가져오기(수정, 삭제 관리자페이지)
    @Transactional(readOnly = true)
    public AdminTodayNewsDetailResponse getTodayNewsDetail(Long id) {
        TodayNews todayNews = todayNewsRepository.findById(id)
                .orElseThrow(() -> new TodayNewsNotFoundException("오늘의 뉴스를 찾을 수 없습니다."));

        return new AdminTodayNewsDetailResponse(todayNews.getSummaryTitle(), todayNews.getSummaryText(), todayNews.getIsPublished(), todayNews.getCreatedAt(), todayNews.getImageKey());
    }

    // 관리자: 오늘의 뉴스 수정
    @Transactional
    public void updateTodayNews(Long id, AdminTodayNewsUpdateRequest dto) {
        TodayNews todayNews = todayNewsRepository.findById(id)
                .orElseThrow(() -> new TodayNewsNotFoundException("오늘의 뉴스를 찾을 수 없습니다."));

        todayNews.update(dto.getSummaryTitle(), dto.getSummaryText(), dto.getIsPublished());
    }

    // 관리자: 오늘의 뉴스 삭제
    @Transactional
    public void deleteTodayNews(Long id) {
        TodayNews todayNews = todayNewsRepository.findById(id)
                .orElseThrow(() -> new TodayNewsNotFoundException("오늘의 뉴스를 찾을 수 없습니다."));

        todayNewsRepository.delete(todayNews);
    }

    // ai 이미지 생성
    public AdminImageGenerateResponse generateImage(String text) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException("이미지 생성 프롬프트를 입력해주세요.");
        }

        return todayNewsAiClient.generateImage(text);
    }

    // 이미지 url db에 저장
    @Transactional
    public void updateImage(Long id, String imageKey) {
        TodayNews todayNews = todayNewsRepository.findById(id)
                .orElseThrow(() -> new TodayNewsNotFoundException("요약을 찾을 수 없습니다."));

        todayNews.updateImageKey(imageKey);
    }
}
