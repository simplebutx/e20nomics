package com.htm.e20nomics.todaynews.ai;

import com.htm.e20nomics.todaynews.dto.AdminImageGenerateResponse;
import com.htm.e20nomics.todaynews.dto.AdminTodayNewsGenerateResponse;
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

// today news 전용 AI 호출 클래스 (Python AI 서비스와 통신하는 역할)
@Component
@RequiredArgsConstructor
public class TodayNewsAiClient {

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    @Value("${ai.service.base-url}")
    private String aiServiceBaseUrl;

    private final RestTemplate restTemplate;

    public AdminTodayNewsGenerateResponse generateSummary(String text) {
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("model", model);
        reqBody.put("text", text);

        ResponseEntity<TodayNewsAiResponse> response = restTemplate.postForEntity(
                aiServiceBaseUrl + "/internal/todaynews/generate",
                new HttpEntity<>(reqBody, createJsonHeaders()),
                TodayNewsAiResponse.class
        );

        TodayNewsAiResponse body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("AI service returned an empty response body.");
        }
        return new AdminTodayNewsGenerateResponse(body.summaryTitle(), body.summaryText(), body.canSave());
    }

    public AdminImageGenerateResponse generateImage(String text) {
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("model", model);
        reqBody.put("text", text);

        ResponseEntity<TodayNewsImageAiResponse> response = restTemplate.postForEntity(
                aiServiceBaseUrl + "/internal/todaynews/generate-image",
                new HttpEntity<>(reqBody, createJsonHeaders()),
                TodayNewsImageAiResponse.class
        );

        TodayNewsImageAiResponse body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("AI service returned an empty response body.");
        }
        return new AdminImageGenerateResponse(body.imageBase64());
    }

    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();    // 헤더 객체 생성
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
