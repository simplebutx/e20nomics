package com.htm.e20nomics.briefing.service;

import com.htm.e20nomics.ai.summarization.OpenAiClient;
import com.htm.e20nomics.briefing.domain.Briefing;
import com.htm.e20nomics.briefing.domain.BriefingSourceType;
import com.htm.e20nomics.briefing.repository.BriefingRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BriefingService {

    private final BriefingRepository briefingRepository;
    private final OpenAiClient openAiClient;

    @Value("${app.briefing.max-length:3000}")
    private int maxLen;

    @Transactional
    public Briefing createUserBriefing(String text) {
        String normalized = normalize(text);

        if (normalized.length() > maxLen) {
            // 저장 없이 예외로 처리해도 되는데, 테스트용으론 그냥 메시지 반환도 OK
            String msg = "입력 텍스트가 너무 깁니다. (최대 " + maxLen + "자)";
            return briefingRepository.save(new Briefing(BriefingSourceType.USER_INPUT, normalized, msg));
        }

        String summary = openAiClient.summarizeKorean3Lines(normalized);

        return briefingRepository.save(
                new Briefing(BriefingSourceType.USER_INPUT, normalized, summary)
        );
    }

    private String normalize(String text) {
        if (text == null) return "";
        return text.trim().replaceAll("\\s+", " ");
    }
}