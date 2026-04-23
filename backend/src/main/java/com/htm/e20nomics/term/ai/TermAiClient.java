package com.htm.e20nomics.term.ai;

import com.htm.e20nomics.term.dto.AdminTermGenerateResponse;
import com.htm.e20nomics.term.dto.MyTermGenerateResponse;
import com.htm.e20nomics.user.enums.TermDifficulty;
import com.htm.e20nomics.user.enums.TermLength;
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

// term 전용 AI 호출 클래스 (Python AI 서비스와 통신하는 역할)
@Component
@RequiredArgsConstructor
public class TermAiClient {

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    @Value("${ai.service.base-url}")
    private String aiServiceBaseUrl;

    // RestTemplate: Spring에서 다른 서버에 HTTP 요청 보낼때 쓰는 클래스
    private final RestTemplate restTemplate;

    // "내 단어 정의 생성" FastApi로 요청보내기
    public MyTermGenerateResponse generateMyTerm(String text,
                                                 TermLength termLength,
                                                 TermDifficulty termDifficulty,
                                                 boolean includeExample,
                                                 boolean includeRelatedConcept) {
        // 요청 body 생성
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("model", model);
        reqBody.put("text", text);
        reqBody.put("term_length", termLength.getPromptValue());
        reqBody.put("term_difficulty", termDifficulty.getPromptValue());
        reqBody.put("include_example", includeExample);
        reqBody.put("include_related_concept", includeRelatedConcept);

        // restTemplate.postForEntity(경로, 요청내용, 응답형태) : 스프링에서 HTTP 요청을 보내는 메서드
        ResponseEntity<TermAiResponse> response = restTemplate.postForEntity(
                aiServiceBaseUrl + "/internal/terms/my/generate",
                new HttpEntity<>(reqBody, createJsonHeaders()),
                TermAiResponse.class
        );

        // 응답에서 바디를 꺼내기
        TermAiResponse body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("AI service returned an empty response body.");
        }

        // 프론트에 돌려줄 DTO 형태로 변환
        return new MyTermGenerateResponse(body.term(), body.definition(), body.canSave());
    }

    // "관리자 단어 생성" FastApi에 요청보내기
    public AdminTermGenerateResponse generateAdminTerm(String text) {
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("model", model);
        reqBody.put("text", text);

        ResponseEntity<TermAiResponse> response = restTemplate.postForEntity(
                aiServiceBaseUrl + "/internal/terms/admin/generate",
                new HttpEntity<>(reqBody, createJsonHeaders()),
                TermAiResponse.class
        );

        TermAiResponse body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("AI service returned an empty response body.");
        }
        return new AdminTermGenerateResponse(body.term(), body.definition(), body.canSave());
    }

    // HTTP 헤더를 만들어서 반환하는 함수
    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();    // 헤더 객체 생성
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
