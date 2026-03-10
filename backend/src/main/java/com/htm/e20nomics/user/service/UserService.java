package com.htm.e20nomics.user.service;

import com.htm.e20nomics.summary.domain.Summary;
import com.htm.e20nomics.summary.repository.SummaryRepository;
import com.htm.e20nomics.term.domain.TermScope;
import com.htm.e20nomics.term.dto.MyTermResponse;
import com.htm.e20nomics.term.repository.TermRepository;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.user.dto.MyPageResponse;
import com.htm.e20nomics.user.dto.MySummariesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SummaryRepository summaryRepository;
    private final TermRepository termRepository;

    public List<MySummariesResponse> mySummaries(Long userId) {
        return summaryRepository.findAllByAuthorId(userId).stream()
                .map(summary -> new MySummariesResponse(summary.getId(), summary.getOriginalText(), summary.getSummaryText(),
                        summary.getAuthor().getUserName(), summary.getCreatedAt(), summary.getUpdatedAt()))
                .toList();
    }

    public List<MyTermResponse> myTerms(Long userId) {
        return termRepository.findAllByTermScopeOrAuthorId(TermScope.PERSONAL, userId).stream()
                .map(term -> new MyTermResponse(term.getId(), term.getTerm(), term.getDefinition()))
                .toList();
    }
}
