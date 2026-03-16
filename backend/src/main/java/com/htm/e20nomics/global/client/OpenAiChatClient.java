package com.htm.e20nomics.global.client;

import com.htm.e20nomics.global.dto.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 외부 API 호출 전용 클래스 (OpenAi 서버와 통신하는 역할)
@Component
@RequiredArgsConstructor
public class OpenAiChatClient {

    @Value("${openai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public String summarizeWithChatCompletions(String prompt) {

        // OpenAI API에 보낼 JSON 요청(바디)을 자바 객체로 만드는 과정
        // 자바에서는 JSON을 직접 문자열로 만들지 않고 Map/List 구조로 만든다음
        // Spring이 JSON으로 변환해준다
        Map<String, Object> reqBody = new HashMap<>();   // JSON 요청을 담을 Map 생성
        reqBody.put("model", "gpt-4o-mini");    // 사용할 모델

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "user", "content", prompt));
        reqBody.put("messages", messages);

        // reqBody
        // ├ model : "gpt-4o-mini"
        // └ messages
        //      └ { role:user, content:prompt }



        // 헤더
        HttpHeaders headers = new HttpHeaders();    // 헤더 객체 생성
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(reqBody, headers);

//        HttpEntity
//         ├ Headers
//         │   ├ Content-Type: application/json
//         │   └ Authorization: Bearer sk-xxx
//         │
//         └ Body
//             ├ model: gpt-4o-mini
//             └ messages: [...]



        // 실제로 OpenAI 서버에 HTTP 요청을 보내는 부분
        ResponseEntity<OpenAiResponse> response = restTemplate.postForEntity(    // postForEntity = HTTP POST요청 보내기
                "https://api.openai.com/v1/chat/completions",
                request,    // 아까 만든 HTTP 요청 객체
                OpenAiResponse.class    // 응답(JSON)을 dto으로 변환해라
        );




        // message의 content 뽑기
        OpenAiResponse body = response.getBody();
        if (body == null || body.getChoices().isEmpty()) {
            throw new IllegalStateException("OpenAI 응답이 비었습니다.");
        }

        return body.getChoices().get(0).getMessage().getContent();


        // OpenAI 응답을 자바에서 꺼내 쓰기 위해 단계적으로 파싱하는 과정 (JSON -> Map/List -> 필요한값 꺼내기)
//        Map<?, ?> resBody = response.getBody();
//        if (resBody == null) throw new IllegalStateException("OpenAI 응답 바디가 비었습니다.");
//
//        List<?> choices = (List<?>) resBody.get("choices");
//        if (choices == null || choices.isEmpty()) throw new IllegalStateException("OpenAI choices가 비었습니다.");
//
//        Map<?, ?> first = (Map<?, ?>) choices.get(0);
//        Map<?, ?> message = (Map<?, ?>) first.get("message");
//        if (message == null) throw new IllegalStateException("OpenAI message가 비었습니다.");
//
//        return (String) message.get("content");
    }

}
