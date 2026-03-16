package com.htm.e20nomics.term.service;

import com.htm.e20nomics.global.client.OpenAiChatClient;
import com.htm.e20nomics.global.exception.AdminTermNotFoundException;
import com.htm.e20nomics.global.exception.DuplicateAdminTermException;
import com.htm.e20nomics.term.domain.AdminTerm;
import com.htm.e20nomics.term.dto.AdminTermCreateRequest;
import com.htm.e20nomics.term.dto.AdminTermGenerateResponse;
import com.htm.e20nomics.term.dto.AdminTermResponse;
import com.htm.e20nomics.term.dto.MyTermGenerateResponse;
import com.htm.e20nomics.term.repository.AdminTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminTermService {

    private final OpenAiChatClient openAiChatClient;
    private final AdminTermRepository adminTermRepository;

    public AdminTermGenerateResponse generateTerm(String text) {
        if (!StringUtils.hasText(text)) {
            return new AdminTermGenerateResponse("", "단어를 입력하세요.", false);
        }

        String term = text.trim();

        if (!isEconomicTerm(term)) {
            return new AdminTermGenerateResponse(term, "경제와 관련된 용어가 아닙니다.", false);
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
                return new AdminTermGenerateResponse(term, "정의 생성에 실패했습니다.", false);
            }

            String definition = result.trim()
                    .replaceAll("^\"|\"$", "")
                    .replaceAll("\\n+", " ");

            return new AdminTermGenerateResponse(term, definition, true);

        } catch (Exception e) {
            return new AdminTermGenerateResponse(term, "정의 생성 중 오류가 발생했습니다.", false);
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