package com.htm.e20nomics.summary.ai;

import com.htm.e20nomics.summary.dto.SummaryGenerateResponse;
import com.htm.e20nomics.user.enums.SummaryDifficulty;
import com.htm.e20nomics.user.enums.SummaryExplainStyle;
import com.htm.e20nomics.user.enums.SummaryFormat;
import com.htm.e20nomics.user.enums.SummaryLength;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// summary 전용 AI 호출 클래스 (Python AI 서비스와 통신하는 역할)
@Component
@RequiredArgsConstructor
public class SummaryAiClient {

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    @Value("${ai.service.base-url}")
    private String aiServiceBaseUrl;

    private final RestTemplate restTemplate;

    public SummaryGenerateResponse summarize(String text,
                                             SummaryLength summaryLength,
                                             SummaryDifficulty summaryDifficulty,
                                             SummaryFormat summaryFormat,
                                             SummaryExplainStyle summaryExplainStyle) {
        // summary 생성에 필요한 원문과 사용자 설정만 Python AI 서비스로 전달
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("model", model);
        reqBody.put("text", text);
        reqBody.put("summary_length", summaryLength.getPromptValue());
        reqBody.put("summary_difficulty", summaryDifficulty.getPromptValue());
        reqBody.put("summary_format", summaryFormat.getPromptValue());
        reqBody.put("summary_explain_style", summaryExplainStyle.getPromptValue());

        ResponseEntity<SummaryAiResponse> response = restTemplate.postForEntity(
                aiServiceBaseUrl + "/internal/summary/generate",
                new HttpEntity<>(reqBody, createJsonHeaders()),
                SummaryAiResponse.class
        );

        SummaryAiResponse body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("AI service returned an empty response body.");
        }
        return new SummaryGenerateResponse(body.summaryTitle(), body.summaryText(), body.canSave());
    }

    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();    // 헤더 객체 생성
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
