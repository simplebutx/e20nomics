package com.htm.e20nomics.global.client;

import com.htm.e20nomics.global.dto.OpenAiImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAiImageClient {

    @Value("${openai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public String generateImage(String prompt) {
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("model", "gpt-image-1");
        reqBody.put("prompt", prompt);
        reqBody.put("size", "1024x1024");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(reqBody, headers);

        ResponseEntity<OpenAiImageResponse> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/images/generations",
                request,
                OpenAiImageResponse.class
        );

        OpenAiImageResponse body = response.getBody();
        if (body == null || body.getData() == null || body.getData().isEmpty()) {
            throw new IllegalStateException("OpenAI 이미지 응답이 비었습니다.");
        }

        return body.getData().get(0).getB64Json();
    }
}