package com.htm.e20nomics.user.service;


import com.htm.e20nomics.global.exception.TermNotFoundException;
import com.htm.e20nomics.global.exception.UserNotFoundException;
import com.htm.e20nomics.term.domain.Term;
import com.htm.e20nomics.term.dto.MyTermResponse;
import com.htm.e20nomics.term.repository.TermRepository;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.user.dto.MyTermDetailResponse;
import com.htm.e20nomics.user.dto.MyTermUpdateRequest;
import com.htm.e20nomics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyTermService {

    private final TermRepository termRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveTerm(String word, String definition, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException());
        Term term = new Term(word, definition, user);
        termRepository.save(term);
    }

    @Transactional(readOnly = true)
    public List<MyTermResponse> myTerms(Long userId) {
        return termRepository.findAllByAuthorId(userId).stream()
                .map(term -> new MyTermResponse(term.getId(), term.getTerm(), term.getDefinition()))
                .toList();
    }

    @Transactional(readOnly = true)
    public MyTermDetailResponse getTermDetail(Long termId) {
        Term term = termRepository.findById(termId)
                .orElseThrow(()-> new TermNotFoundException("단어를 찾을 수 없습니다."));
        return new MyTermDetailResponse(term.getTerm(), term.getDefinition(), term.getCreatedAt());
    }

    @Transactional
    public void updateMyTerm(Long termId, MyTermUpdateRequest dto, Long userId) {
        Term term = termRepository.findById(termId)
                .orElseThrow(() -> new TermNotFoundException("단어를 찾을 수 없습니다."));

        if (!term.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("본인의 단어만 수정할 수 있습니다.");
        }

        term.update(dto.getTerm(), dto.getDefinition());
    }

    @Transactional
    public void deleteMyTerm(Long termId, Long userId) {
        Term term = termRepository.findById(termId)
                .orElseThrow(() -> new TermNotFoundException("단어를 찾을 수 없습니다."));
        if (!term.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("본인의 단어만 삭제할 수 있습니다.");
        }
        termRepository.delete(term);
    }
}
