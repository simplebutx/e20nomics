package com.htm.e20nomics.term.service;


import com.htm.e20nomics.global.exception.TermNotFoundException;
import com.htm.e20nomics.global.exception.UserNotFoundException;
import com.htm.e20nomics.term.ai.TermAiClient;
import com.htm.e20nomics.term.domain.MyTerm;
import com.htm.e20nomics.term.dto.MyTermDetailResponse;
import com.htm.e20nomics.term.dto.MyTermGenerateResponse;
import com.htm.e20nomics.term.dto.MyTermResponse;
import com.htm.e20nomics.term.dto.MyTermUpdateRequest;
import com.htm.e20nomics.term.repository.MyTermRepository;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.user.domain.UserPreference;
import com.htm.e20nomics.user.enums.TermDifficulty;
import com.htm.e20nomics.user.enums.TermLength;
import com.htm.e20nomics.user.repository.UserPreferenceRepository;
import com.htm.e20nomics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyTermService {

    private final TermAiClient termAiClient;
    private final MyTermRepository myTermRepository;
    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    public MyTermGenerateResponse generateTerm(String text, Long userId) {
        if (!StringUtils.hasText(text)) {
            return new MyTermGenerateResponse("", "용어를 입력해주세요.", false);
        }

        UserPreference userPreference = userPreferenceRepository.findByUserId(userId);
        if (userPreference == null) {
            return new MyTermGenerateResponse("", "사용자 선호도 설정을 찾을 수 없습니다.", false);
        }
        TermLength termLength = userPreference.getTermLength();
        TermDifficulty termDifficulty = userPreference.getTermDifficulty();
        boolean includeExample = userPreference.isIncludeExample();
        boolean includeRelatedConcept = userPreference.isIncludeRelatedConcept();

        try {
            return termAiClient.generateMyTerm(
                    text.trim(),
                    termLength,
                    termDifficulty,
                    includeExample,
                    includeRelatedConcept
            );
        } catch (Exception e) {
            return new MyTermGenerateResponse(text.trim(), "정의 생성 중 오류가 발생했습니다.", false);
        }
    }

    @Transactional
    public void saveTerm(String word, String definition, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
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
                .orElseThrow(() -> new TermNotFoundException("용어를 찾을 수 없습니다."));
        return new MyTermDetailResponse(myTerm.getTerm(), myTerm.getDefinition(), myTerm.getCreatedAt());
    }

    @Transactional
    public void updateMyTerm(Long termId, MyTermUpdateRequest dto, Long userId) {
        MyTerm myTerm = myTermRepository.findById(termId)
                .orElseThrow(() -> new TermNotFoundException("용어를 찾을 수 없습니다."));

        if (!myTerm.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("본인의 용어만 수정할 수 있습니다.");
        }

        myTerm.update(dto.getTerm(), dto.getDefinition());
    }

    @Transactional
    public void deleteMyTerm(Long termId, Long userId) {
        MyTerm myTerm = myTermRepository.findById(termId)
                .orElseThrow(() -> new TermNotFoundException("용어를 찾을 수 없습니다."));
        if (!myTerm.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("본인의 용어만 삭제할 수 있습니다.");
        }
        myTermRepository.delete(myTerm);
    }
}
