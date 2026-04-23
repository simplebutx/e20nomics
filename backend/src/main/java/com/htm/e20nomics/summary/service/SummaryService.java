package com.htm.e20nomics.summary.service;

import com.htm.e20nomics.global.exception.SummaryNotFoundException;
import com.htm.e20nomics.global.exception.UserNotFoundException;
import com.htm.e20nomics.summary.ai.SummaryAiClient;
import com.htm.e20nomics.summary.domain.Summary;
import com.htm.e20nomics.summary.dto.SummariesResponse;
import com.htm.e20nomics.summary.dto.SummaryCreateRequest;
import com.htm.e20nomics.summary.dto.SummaryDetailResponse;
import com.htm.e20nomics.summary.dto.SummaryGenerateResponse;
import com.htm.e20nomics.summary.dto.SummaryUpdateRequest;
import com.htm.e20nomics.summary.repository.SummaryRepository;
import com.htm.e20nomics.user.domain.User;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final SummaryAiClient summaryAiClient;
    private final SummaryRepository summaryRepository;
    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    public SummaryGenerateResponse summarize(String text, Long userId) {

        if (!StringUtils.hasText(text)) {    // null, 공백, 길이를 모두 한번에 체크
            return new SummaryGenerateResponse("", "요약할 텍스트가 비어있습니다.", false);
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

        try {
            // 프롬프트 생성, 경제뉴스 판별, JSON 파싱은 Python AI 서비스에서 처리
            return summaryAiClient.summarize(
                    text,
                    summaryLength,
                    summaryDifficulty,
                    summaryFormat,
                    summaryExplainStyle
            );

        } catch (Exception e) {
            e.printStackTrace();
            return new SummaryGenerateResponse("", "제목/요약에 실패했습니다.", false);
        }
    }


    @Transactional  // 개인 뉴스 저장
    public void saveSummary(SummaryCreateRequest dto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

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
                .orElseThrow(() -> new SummaryNotFoundException("요약을 찾을 수 없습니다."));
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
