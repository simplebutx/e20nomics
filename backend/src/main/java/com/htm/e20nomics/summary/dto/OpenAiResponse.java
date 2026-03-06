package com.htm.e20nomics.summary.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OpenAiResponse {
    private List<Choice> choices;

    @Getter
    public static class Choice {
        private Message message;
    }

    @Getter
    public static class Message {
        private String content;
        private String role;
    }
}

// 응답의 body는 이런 형태로 온다

//{
//    "choices": [
//      {
//         "message": {
//            "role": "assistant",
//            "content": "요약 결과"
//       }
//    }
//  ]
//}