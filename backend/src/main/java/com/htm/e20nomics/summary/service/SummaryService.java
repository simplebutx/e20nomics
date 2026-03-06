package com.htm.e20nomics.summary.service;


import ch.qos.logback.core.util.StringUtil;
import com.htm.e20nomics.summary.client.OpenAiChatClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final OpenAiChatClient openAiChatClient;

    public String summarize(String text) {
        if (!StringUtils.hasText(text)) {    // null, 공백, 길이를 모두 한번에 체크
            return "요약할 텍스트가 비어있습니다.";
        }

        // 유저가 저장한 preference를 조회해서 prompt에 스타일 반영

        String prompt = """
                다음 텍스트를 한국어로 3줄 요약해줘.
                추측하지 말고, 주어진 내용만 사용해.
                
                텍스트:
                %s
                """.formatted(text);

        return openAiChatClient.summarizeWithChatCompletions(prompt);
    }
}
