package com.htm.e20nomics.admin.service;

import com.htm.e20nomics.admin.dto.AdminCreateTermRequest;
import com.htm.e20nomics.admin.dto.AdminTermResponse;
import com.htm.e20nomics.admin.dto.AdminUserListResponse;
import com.htm.e20nomics.global.exception.UserNotFoundException;
import com.htm.e20nomics.term.domain.Term;
import com.htm.e20nomics.term.domain.TermScope;
import com.htm.e20nomics.term.repository.TermRepository;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final TermRepository termRepository;

    @Transactional(readOnly = true)
    public List<AdminUserListResponse> getUserList() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(user -> new AdminUserListResponse
                        (user.getId(), user.getEmail(), user.getUserName(), user.getDisplayName(),
                                user.getRole(), user.getCreatedAt()))
                .toList();
    }

    @Transactional
    public void createGlobalTerm(AdminCreateTermRequest dto, Long adminId) {
        User user = userRepository.findById(adminId)
                .orElseThrow(()-> new UserNotFoundException());
        Term term = new Term(dto.getTerm(), dto.getDefinition(), user, TermScope.GLOBAL);
        termRepository.save(term);
    }

    @Transactional(readOnly = true)
    public List<AdminTermResponse> getGlobalTerms() {
        List<Term> globalTerms = termRepository.findAllByTermScope(TermScope.GLOBAL);
        return globalTerms.stream()
                .map(terms-> new AdminTermResponse(terms.getId(), terms.getTerm(), terms.getDefinition()))
                .toList();
    }
}
