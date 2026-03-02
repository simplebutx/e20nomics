package com.htm.e20nomics;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

// React -> Spring -> OpenAI -> Spring -> React
@RestController
public class SummaryController {

    @Value("${openai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/api/summaries")
    public Map<String, String> summarize(@RequestBody Map<String, String> body) {

        String text = body.getOrDefault("text", "");

        // ✅ 프롬프트 (아주 단순)
        String prompt = """
다음 텍스트를 한국어로 3줄 요약해줘.
추측하지 말고, 주어진 내용만 사용해.

텍스트:
%s
""".formatted(text);

        // ✅ OpenAI 요청 바디 (Chat Completions 형태)
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("model", "gpt-4o-mini");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "user", "content", prompt));
        reqBody.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(reqBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                request,
                Map.class
        );

        // ✅ 응답 파싱 (choices[0].message.content)
        Map<?, ?> resBody = response.getBody();
        List<?> choices = (List<?>) resBody.get("choices");
        Map<?, ?> first = (Map<?, ?>) choices.get(0);
        Map<?, ?> message = (Map<?, ?>) first.get("message");
        String summary = (String) message.get("content");

        return Map.of("summary", summary);
    }
}
