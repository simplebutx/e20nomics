package com.htm.e20nomics.ai.summarization;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final RestTemplate restTemplate;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    public String summarizeKorean3Lines(String text) {
        String prompt = """
다음 텍스트를 한국어로 3줄 요약해줘.
- 추측하지 말고, 주어진 내용만 사용해.
- 불필요한 서론 없이 바로 요약만 출력해.

텍스트:
%s
""".formatted(text);

        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("model", model);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "user", "content", prompt));
        reqBody.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(reqBody, headers);

        ResponseEntity<Map> res = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                entity,
                Map.class
        );

        Map body = res.getBody();
        if (body == null) return "요약 실패: 빈 응답";

        List choices = (List) body.get("choices");
        if (choices == null || choices.isEmpty()) return "요약 실패: choices 없음";

        Map first = (Map) choices.get(0);
        Map message = (Map) first.get("message");
        String content = (String) message.get("content");

        return content == null ? "요약 실패: content 없음" : content.trim();
    }
}
