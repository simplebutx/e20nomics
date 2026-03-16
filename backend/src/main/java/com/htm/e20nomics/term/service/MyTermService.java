package com.htm.e20nomics.term.service;


import com.htm.e20nomics.global.client.OpenAiChatClient;
import com.htm.e20nomics.global.exception.TermNotFoundException;
import com.htm.e20nomics.global.exception.UserNotFoundException;
import com.htm.e20nomics.term.domain.MyTerm;
import com.htm.e20nomics.term.dto.MyTermGenerateResponse;
import com.htm.e20nomics.term.dto.MyTermResponse;
import com.htm.e20nomics.term.repository.MyTermRepository;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.term.dto.MyTermDetailResponse;
import com.htm.e20nomics.term.dto.MyTermUpdateRequest;
import com.htm.e20nomics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyTermService {

    private final OpenAiChatClient openAiChatClient;
    private final MyTermRepository myTermRepository;
    private final UserRepository userRepository;

    public MyTermGenerateResponse generateTerm(String text) {
        if (!StringUtils.hasText(text)) {
            return new MyTermGenerateResponse("", "단어를 입력하세요.", false);
        }

        String term = text.trim();

        if (!isEconomicTerm(term)) {
            return new MyTermGenerateResponse(term, "경제와 관련된 용어가 아닙니다.", false);
        }

        String prompt = """
            너는 경제 용어를 쉽게 설명하는 도우미다.
            사용자가 입력한 용어를 초보자도 이해할 수 있게 한국어로 간단하고 정확하게 정의해라.
            설명은 2~3문장 이내로 작성하고, 너무 어려운 전문용어는 피하라.
            입력한 용어 자체를 그대로 반복하기보다 의미를 자연스럽게 설명하라.
            불필요한 인사말, 제목, 번호, 목록 없이 정의 내용만 출력하라.

            용어: %s
            """.formatted(term);

        try {
            String result = openAiChatClient.summarizeWithChatCompletions(prompt);

            if (!StringUtils.hasText(result)) {
                return new MyTermGenerateResponse(term, "정의 생성에 실패했습니다.", false);
            }

            String definition = result.trim()
                    .replaceAll("^\"|\"$", "")
                    .replaceAll("\\n+", " ");

            return new MyTermGenerateResponse(term, definition, true);

        } catch (Exception e) {
            return new MyTermGenerateResponse(term, "정의 생성 중 오류가 발생했습니다.", false);
        }
    }

    private boolean isEconomicTerm(String text) {
        String prompt = """
            다음 텍스트가 경제 용어인지 판별해줘.
            경제와 관련된 용어이면 YES
            아니면 NO
            애매하면 그냥 YES
            경제 용어의 범위는 후하게 잡아라.
            이 검사는 이상한 입력을 거르기 위한 용도다.
            다른 말 하지 말고 YES 또는 NO만 답해라.

            텍스트: %s
            """.formatted(text);

        try {
            String result = openAiChatClient.summarizeWithChatCompletions(prompt);

            if (!StringUtils.hasText(result)) {
                return false;
            }

            String normalized = result.trim().toUpperCase();

            return normalized.startsWith("YES");
        } catch (Exception e) {
            return false;
        }
    }


    @Transactional
    public void saveTerm(String word, String definition, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException());
        MyTerm myTerm = new MyTerm(word, definition, user);
        myTermRepository.save(myTerm);
    }

    @Transactional(readOnly = true)
    public List<MyTermResponse> myTerms(Long userId) {
        return myTermRepository.findAllByAuthorId(userId).stream()
                .map(myTerm -> new MyTermResponse(myTerm.getId(), myTerm.getTerm(), myTerm.getDefinition()))
                .toList();
    }

    @Transactional(readOnly = true)
    public MyTermDetailResponse getTermDetail(Long termId) {
        MyTerm myTerm = myTermRepository.findById(termId)
                .orElseThrow(()-> new TermNotFoundException("단어를 찾을 수 없습니다."));
        return new MyTermDetailResponse(myTerm.getTerm(), myTerm.getDefinition(), myTerm.getCreatedAt());
    }

    @Transactional
    public void updateMyTerm(Long termId, MyTermUpdateRequest dto, Long userId) {
        MyTerm myTerm = myTermRepository.findById(termId)
                .orElseThrow(() -> new TermNotFoundException("단어를 찾을 수 없습니다."));

        if (!myTerm.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("본인의 단어만 수정할 수 있습니다.");
        }

        myTerm.update(dto.getTerm(), dto.getDefinition());
    }

    @Transactional
    public void deleteMyTerm(Long termId, Long userId) {
        MyTerm myTerm = myTermRepository.findById(termId)
                .orElseThrow(() -> new TermNotFoundException("단어를 찾을 수 없습니다."));
        if (!myTerm.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("본인의 단어만 삭제할 수 있습니다.");
        }
        myTermRepository.delete(myTerm);
    }
}
