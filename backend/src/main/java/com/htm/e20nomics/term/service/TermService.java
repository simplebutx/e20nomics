package com.htm.e20nomics.term.service;

import com.htm.e20nomics.global.exception.UserNotFoundException;
import com.htm.e20nomics.term.domain.Term;
import com.htm.e20nomics.term.domain.TermScope;
import com.htm.e20nomics.term.repository.TermRepository;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TermService {

    private final UserRepository userRepository;
    private final TermRepository termRepository;

    @Transactional
    public void saveTerm(String word, String definition, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException());
        Term term = new Term(word, definition, user, TermScope.PERSONAL);
        termRepository.save(term);
    }
}
