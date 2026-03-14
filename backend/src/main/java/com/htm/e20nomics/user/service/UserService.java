package com.htm.e20nomics.user.service;

import com.htm.e20nomics.global.exception.SummaryNotFoundException;
import com.htm.e20nomics.global.exception.TermNotFoundException;
import com.htm.e20nomics.summary.domain.Summary;
import com.htm.e20nomics.summary.repository.SummaryRepository;
import com.htm.e20nomics.term.domain.Term;
import com.htm.e20nomics.term.domain.TermScope;
import com.htm.e20nomics.term.dto.MyTermResponse;
import com.htm.e20nomics.term.repository.TermRepository;
import com.htm.e20nomics.user.dto.MySummariesResponse;
import com.htm.e20nomics.user.dto.MySummaryDetailResponse;
import com.htm.e20nomics.user.dto.MyTermDetailResponse;
import com.htm.e20nomics.user.dto.MyTermUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// 내기사 가져오기, 내기사 상세페이지가져오기, 내단어 가져오기, 내단어 상세페이지 가져오기
// 내단어 수정하기, 내단어 삭제하기
@Service
@RequiredArgsConstructor
public class UserService {

    private final SummaryRepository summaryRepository;
    private final TermRepository termRepository;

    @Transactional(readOnly = true)
    public List<MySummariesResponse> mySummaries(Long userId) {
        return summaryRepository.findAllByAuthorId(userId).stream()
                .map(summary -> new MySummariesResponse(summary.getId(), summary.getSummaryTitle(), summary.getSummaryText(),
                       summary.getCreatedAt(), summary.getUpdatedAt()))
                .toList();
    }

    @Transactional(readOnly = true)
    public MySummaryDetailResponse getSummaryDetail(Long summaryId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(()-> new SummaryNotFoundException("요약을 찾을 수 없습니다."));
        return new MySummaryDetailResponse(summary.getSummaryTitle(), summary.getSummaryText(), summary.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public List<MyTermResponse> myTerms(Long userId) {
        return termRepository.findAllByTermScopeOrAuthorId(TermScope.GLOBAL, userId).stream()
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
