package com.htm.e20nomics.term.service;


import com.htm.e20nomics.global.exception.TermNotFoundException;
import com.htm.e20nomics.global.exception.UserNotFoundException;
import com.htm.e20nomics.term.domain.MyTerm;
import com.htm.e20nomics.term.dto.MyTermResponse;
import com.htm.e20nomics.term.repository.MyTermRepository;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.term.dto.MyTermDetailResponse;
import com.htm.e20nomics.term.dto.MyTermUpdateRequest;
import com.htm.e20nomics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyTermService {

    private final MyTermRepository myTermRepository;
    private final UserRepository userRepository;

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
