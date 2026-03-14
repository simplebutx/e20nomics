package com.htm.e20nomics.user.service;

import com.htm.e20nomics.global.exception.SummaryNotFoundException;
import com.htm.e20nomics.summary.domain.Summary;
import com.htm.e20nomics.summary.repository.SummaryRepository;
import com.htm.e20nomics.term.domain.TermScope;
import com.htm.e20nomics.term.dto.MyTermResponse;
import com.htm.e20nomics.term.repository.TermRepository;
import com.htm.e20nomics.user.dto.MySummariesResponse;
import com.htm.e20nomics.user.dto.MySummaryDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<MyTermResponse> myTerms(Long userId) {
        return termRepository.findAllByTermScopeOrAuthorId(TermScope.PERSONAL, userId).stream()
                .map(term -> new MyTermResponse(term.getId(), term.getTerm(), term.getDefinition()))
                .toList();
    }

    @Transactional(readOnly = true)
    public MySummaryDetailResponse getDetail(Long summaryId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(()-> new SummaryNotFoundException("요약을 찾을 수 없습니다."));
        return new MySummaryDetailResponse(summary.getSummaryTitle(), summary.getSummaryText(), summary.getCreatedAt());
    }
}
