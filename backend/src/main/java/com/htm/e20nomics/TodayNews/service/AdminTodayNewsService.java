package com.htm.e20nomics.TodayNews.service;

import com.htm.e20nomics.TodayNews.domain.TodayNews;
import com.htm.e20nomics.TodayNews.dto.*;
import com.htm.e20nomics.TodayNews.repository.TodayNewsRepository;
import com.htm.e20nomics.global.exception.TodayNewsNotFoundException;
import com.htm.e20nomics.global.client.OpenAiChatClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminTodayNewsService {

    private final OpenAiChatClient openAiChatClient;
    private final TodayNewsRepository todayNewsRepository;

    // 관리자: 오늘의 뉴스 생성하기
    @Transactional
    public AdminTodayNewsGenerateResponse summarize(String text) {
        if (!StringUtils.hasText(text)) {    // null, 공백, 길이를 모두 한번에 체크
            return new AdminTodayNewsGenerateResponse("", "요약할 텍스트가 비어있습니다.", false);
        }
        if (!isEconomicNews(text)) {
            return new AdminTodayNewsGenerateResponse("", "경제 뉴스가 아니므로 요약할 수 없습니다.", false);
        }

        String prompt = """
                다음 텍스트를 바탕으로 제목과 요약을 생성해줘.
                
                규칙:
                - 추측하지 말고 주어진 내용만 사용
                - 제목은 짧고 자연스럽게
                - 요약은 한국어 3줄
                - 반드시 아래 JSON 형식으로만 반환
                - 다른 설명은 절대 하지 말 것
                
                {
                  "title": "...",
                  "summary": "..."
                }
                
                텍스트:
                %s
                """.formatted(text);

        String result = openAiChatClient.summarizeWithChatCompletions(prompt);

        try {
            result = result.trim()
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> map = objectMapper.readValue(result, new TypeReference<Map<String, String>>() {
            });

            String summaryTitle = map.get("title");
            String summaryText = map.get("summary");

            return new AdminTodayNewsGenerateResponse(summaryTitle, summaryText, true);

        } catch (Exception e) {
            e.printStackTrace();
            return new AdminTodayNewsGenerateResponse("", "제목/요약에 실패했습니다.", false);
        }
    }

    @Transactional
    // 경제뉴스 여부 검사
    private boolean isEconomicNews(String text) {
        String prompt = """
                다음 텍스트가 경제 뉴스인지 판별해줘.
                경제 뉴스이면 YES
                아니면 NO
                애매하면 NO
                다른 말 하지 말고 YES 또는 NO만 답해.
                
                텍스트:
                %s
                """.formatted(text);

        String result = openAiChatClient.summarizeWithChatCompletions(prompt);
        return "YES".equalsIgnoreCase(result.trim());   // yes가 들어있으면 true, no면 false 반환
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

    // 관리자: 오늘의 뉴스 상세페이지 가져오기 (수정, 삭제 가능페이지)
    @Transactional(readOnly = true)
    public AdminTodayNewsDetailResponse getTodayNewsDetail(Long id) {
        TodayNews todayNews = todayNewsRepository.findById(id)
                .orElseThrow(() -> new TodayNewsNotFoundException("오늘의 뉴스를 찾을 수 없습니다."));

        return new AdminTodayNewsDetailResponse(todayNews.getSummaryTitle(), todayNews.getSummaryText(), todayNews.getIsPublished(), todayNews.getCreatedAt());
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

}
