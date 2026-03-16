package com.htm.e20nomics.term.service;

import com.htm.e20nomics.global.exception.AdminTermNotFoundException;
import com.htm.e20nomics.global.exception.DuplicateAdminTermException;
import com.htm.e20nomics.term.domain.AdminTerm;
import com.htm.e20nomics.term.dto.AdminTermCreateRequest;
import com.htm.e20nomics.term.dto.AdminTermResponse;
import com.htm.e20nomics.term.repository.AdminTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminTermService {

    private final AdminTermRepository adminTermRepository;

    @Transactional
    public void createAdminTerm(AdminTermCreateRequest request) {
        validateDuplicateTerm(request.getTerm());

        AdminTerm adminTerm = new AdminTerm(request.getTerm(), request.getDefinition());
        adminTermRepository.save(adminTerm);
    }

    public List<AdminTermResponse> getAdminTerms() {
        return adminTermRepository.findAll().stream()
                .map(AdminTermResponse::new)
                .toList();
    }

    public AdminTermResponse getAdminTerm(Long termId) {
        AdminTerm adminTerm = adminTermRepository.findById(termId)
                .orElseThrow(() -> new AdminTermNotFoundException("관리자 단어를 찾을 수 없습니다."));
        return new AdminTermResponse(adminTerm);
    }

    @Transactional
    public void updateAdminTerm(Long termId, AdminTermCreateRequest request) {
        AdminTerm adminTerm = adminTermRepository.findById(termId)
                .orElseThrow(() -> new AdminTermNotFoundException("관리자 단어를 찾을 수 없습니다."));

        if (!adminTerm.getTerm().equals(request.getTerm())
                && adminTermRepository.existsByTerm(request.getTerm())) {
            throw new DuplicateAdminTermException("이미 존재하는 단어입니다.");
        }

        adminTerm.update(request.getTerm(), request.getDefinition());
    }

    @Transactional
    public void deleteAdminTerm(Long termId) {
        AdminTerm adminTerm = adminTermRepository.findById(termId)
                .orElseThrow(() -> new AdminTermNotFoundException("관리자 단어를 찾을 수 없습니다."));

        adminTermRepository.delete(adminTerm);
    }

    private void validateDuplicateTerm(String term) {
        if (adminTermRepository.existsByTerm(term)) {
            throw new DuplicateAdminTermException("이미 존재하는 단어입니다.");
        }
    }
}