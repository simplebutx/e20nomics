package com.htm.e20nomics.summary.service;

import com.htm.e20nomics.global.exception.SummaryNotFoundException;
import com.htm.e20nomics.global.exception.UserNotFoundException;
import com.htm.e20nomics.summary.client.OpenAiChatClient;
import com.htm.e20nomics.summary.domain.CreatedBy;
import com.htm.e20nomics.summary.domain.Summary;
import com.htm.e20nomics.summary.dto.AnnouncementDetailResponse;
import com.htm.e20nomics.summary.dto.AnnouncementsResponse;
import com.htm.e20nomics.summary.dto.SummaryCreateRequest;
import com.htm.e20nomics.summary.dto.SummaryGenerateResponse;
import com.htm.e20nomics.summary.repository.SummaryRepository;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.user.repository.UserRepository;
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
public class SummaryService {

    private final OpenAiChatClient openAiChatClient;
    private final SummaryRepository summaryRepository;
    private final UserRepository userRepository;

    @Transactional
    public SummaryGenerateResponse summarize(String text) {
        if (!StringUtils.hasText(text)) {    // null, 공백, 길이를 모두 한번에 체크
            return new SummaryGenerateResponse("", "요약할 텍스트가 비어있습니다.", false);
        }
        if (!isEconomicNews(text)) {
            return new SummaryGenerateResponse("", "경제 뉴스가 아니므로 요약할 수 없습니다.", false);
        }

        // + 유저가 저장한 preference를 조회해서 prompt에 스타일 반영

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

            System.out.println("title = " + summaryTitle);
            System.out.println("summary = " + summaryText);

            return new SummaryGenerateResponse(summaryTitle, summaryText, true);

        } catch (Exception e) {
            e.printStackTrace();
            return new SummaryGenerateResponse("", "제목/요약에 실패했습니다.", false);
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

    @Transactional  // 개인 뉴스 저장
    public void saveSummary(SummaryCreateRequest dto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException());

        Summary summary = new Summary(dto.getOriginalText(), dto.getSummaryTitle(), dto.getSummaryText(), dto.getMemo(), user, CreatedBy.USER);
        summaryRepository.save(summary);
    }


    @Transactional   // 관리자 뉴스 등록
    public void postAnnouncement(SummaryCreateRequest dto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException());

        Summary summary = new Summary(dto.getOriginalText(), dto.getSummaryTitle(), dto.getSummaryText(), dto.getMemo(), user, CreatedBy.ADMIN);
        summaryRepository.save(summary);
    }

    @Transactional(readOnly = true)
    public List<AnnouncementsResponse> getAnnouncements() {
        List<Summary> lists = summaryRepository.findAllByCreatedBy(CreatedBy.ADMIN);
        return lists.stream().map((list)->new AnnouncementsResponse(list.getId(), list.getSummaryTitle(), list.getSummaryText(), list.getCreatedAt()))
                .toList();
    }

    @Transactional(readOnly = true)
    public AnnouncementDetailResponse getAnnouncementDetail(Long announcementId) {
        Summary summary = summaryRepository.findById(announcementId)
                .orElseThrow(()-> new SummaryNotFoundException("요약을 찾을 수 없습니다."));
        return new AnnouncementDetailResponse(summary.getSummaryTitle(), summary.getSummaryText(), summary.getAuthor().getDisplayName(), summary.getCreatedAt());
    }
}
