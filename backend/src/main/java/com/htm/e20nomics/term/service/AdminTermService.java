package com.htm.e20nomics.term.service;

import com.htm.e20nomics.global.exception.AdminTermNotFoundException;
import com.htm.e20nomics.global.exception.DuplicateAdminTermException;
import com.htm.e20nomics.term.ai.TermAiClient;
import com.htm.e20nomics.term.domain.AdminTerm;
import com.htm.e20nomics.term.dto.AdminTermCreateRequest;
import com.htm.e20nomics.term.dto.AdminTermGenerateResponse;
import com.htm.e20nomics.term.dto.AdminTermResponse;
import com.htm.e20nomics.term.repository.AdminTermRepository;
import com.htm.e20nomics.todaynews.repository.TodayNewsTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminTermService {

    private final TermAiClient termAiClient;
    private final AdminTermRepository adminTermRepository;
    private final TodayNewsTermRepository todayNewsTermRepository;

    public AdminTermGenerateResponse generateTerm(String text) {
        if (!StringUtils.hasText(text)) {
            return new AdminTermGenerateResponse("", "용어를 입력해주세요.", false);
        }

        try {
            return termAiClient.generateAdminTerm(text.trim());
        } catch (Exception e) {
            return new AdminTermGenerateResponse(text.trim(), "정의 생성 중 오류가 발생했습니다.", false);
        }
    }

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
                .orElseThrow(() -> new AdminTermNotFoundException("관리자 용어를 찾을 수 없습니다."));
        return new AdminTermResponse(adminTerm);
    }

    @Transactional
    public void updateAdminTerm(Long termId, AdminTermCreateRequest request) {
        AdminTerm adminTerm = adminTermRepository.findById(termId)
                .orElseThrow(() -> new AdminTermNotFoundException("관리자 용어를 찾을 수 없습니다."));

        if (!adminTerm.getTerm().equals(request.getTerm())
                && adminTermRepository.existsByTerm(request.getTerm())) {
            throw new DuplicateAdminTermException("이미 존재하는 용어입니다.");
        }

        adminTerm.update(request.getTerm(), request.getDefinition());
    }

    @Transactional
    public void deleteAdminTerm(Long termId) {
        AdminTerm adminTerm = adminTermRepository.findById(termId)
                .orElseThrow(() -> new AdminTermNotFoundException("관리자 용어를 찾을 수 없습니다."));

        todayNewsTermRepository.deleteByAdminTermId(termId);
        adminTermRepository.delete(adminTerm);
    }

    private void validateDuplicateTerm(String term) {
        if (adminTermRepository.existsByTerm(term)) {
            throw new DuplicateAdminTermException("이미 존재하는 용어입니다.");
        }
    }
}
