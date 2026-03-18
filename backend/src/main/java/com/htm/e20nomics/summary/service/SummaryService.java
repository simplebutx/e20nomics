package com.htm.e20nomics.summary.service;

import com.htm.e20nomics.global.exception.SummaryNotFoundException;
import com.htm.e20nomics.global.exception.UserNotFoundException;
import com.htm.e20nomics.global.client.OpenAiChatClient;
import com.htm.e20nomics.summary.domain.Summary;
import com.htm.e20nomics.summary.dto.SummaryCreateRequest;
import com.htm.e20nomics.summary.dto.SummaryGenerateResponse;
import com.htm.e20nomics.summary.repository.SummaryRepository;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.summary.dto.SummariesResponse;
import com.htm.e20nomics.summary.dto.SummaryDetailResponse;
import com.htm.e20nomics.summary.dto.SummaryUpdateRequest;
import com.htm.e20nomics.user.domain.UserPreference;
import com.htm.e20nomics.user.enums.SummaryDifficulty;
import com.htm.e20nomics.user.enums.SummaryExplainStyle;
import com.htm.e20nomics.user.enums.SummaryFormat;
import com.htm.e20nomics.user.enums.SummaryLength;
import com.htm.e20nomics.user.repository.UserPreferenceRepository;
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
    private final UserPreferenceRepository userPreferenceRepository;

    public SummaryGenerateResponse summarize(String text, Long userId) {

        if (!StringUtils.hasText(text)) {    // null, 공백, 길이를 모두 한번에 체크
            return new SummaryGenerateResponse("", "요약할 텍스트가 비어있습니다.", false);
        }
        if (!isEconomicNews(text)) {
            return new SummaryGenerateResponse("", "경제 뉴스가 아니므로 요약할 수 없습니다.", false);
        }

        UserPreference userPreference = userPreferenceRepository.findByUserId(userId);
        if (userPreference == null) {
            return new SummaryGenerateResponse("", "사용자 선호도 설정을 찾을 수 없습니다.", false);
        }
        SummaryLength summaryLength = userPreference.getSummaryLength();
        SummaryDifficulty summaryDifficulty = userPreference.getSummaryDifficulty();
        SummaryFormat summaryFormat = userPreference.getSummaryFormat();
        SummaryExplainStyle summaryExplainStyle = userPreference.getSummaryExplainStyle();
        // + 유저가 저장한 preference를 조회해서 prompt에 스타일 반영

        String prompt = """
        주어진 텍스트만 사용해 제목과 요약을 생성하라.
        없는 내용을 추측하거나 추가하지 마라.

        사용자 설정:
        - 길이: %s
        - 난이도: %s
        - 형식: %s
        - 설명 방식: %s

        위 설정을 반영해 제목과 요약을 작성하라.
        제목은 짧고 자연스럽게 작성하라.
        출력은 반드시 아래 JSON만 반환하라.

        {
          "title": "...",
          "summary": "..."
        }

        텍스트:
        %s
        """.formatted(
                summaryLength.getPromptValue(),
                summaryDifficulty.getPromptValue(),
                summaryFormat.getPromptValue(),
                summaryExplainStyle.getPromptValue(),
                text
        );

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

            return new SummaryGenerateResponse(summaryTitle, summaryText, true);

        } catch (Exception e) {
            e.printStackTrace();
            return new SummaryGenerateResponse("", "제목/요약에 실패했습니다.", false);
        }
    }


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

        Summary summary = new Summary(dto.getOriginalText(), dto.getSummaryTitle(), dto.getSummaryText(), dto.getMemo(), user);
        summaryRepository.save(summary);
    }


    @Transactional(readOnly = true)
    public List<SummariesResponse> mySummaries(Long userId) {
        return summaryRepository.findAllByAuthorId(userId).stream()
                .map(summary -> new SummariesResponse(summary.getId(), summary.getSummaryTitle(), summary.getSummaryText(),
                        summary.getCreatedAt(), summary.getUpdatedAt()))
                .toList();
    }

    @Transactional(readOnly = true)
    public SummaryDetailResponse getSummaryDetail(Long summaryId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(()-> new SummaryNotFoundException("요약을 찾을 수 없습니다."));
        return new SummaryDetailResponse(summary.getSummaryTitle(), summary.getSummaryText(), summary.getMemo(), summary.getCreatedAt());
    }

    @Transactional
    public void updateSummary(Long summaryId, Long userId, SummaryUpdateRequest dto) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new SummaryNotFoundException("요약을 찾을 수 없습니다."));

        if (!summary.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("본인의 요약만 수정할 수 있습니다.");
        }

        summary.update(dto.getSummaryTitle(), dto.getSummaryText(), dto.getMemo());
    }

    @Transactional
    public void deleteSummary(Long summaryId, Long userId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new SummaryNotFoundException("요약을 찾을 수 없습니다."));

        if (!summary.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("본인의 요약만 삭제할 수 있습니다.");
        }

        summaryRepository.delete(summary);
    }
}
