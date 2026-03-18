package com.htm.e20nomics.user.dto;


import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.user.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPreferenceCreateRequest {
    private User user;
    private SummaryLength summaryLength;
    private SummaryDifficulty summaryDifficulty;
    private SummaryFormat summaryFormat;
    private SummaryExplainStyle summaryExplainStyle;
    private TermLength termLength;
    private TermDifficulty termDifficulty;
    private boolean includeExample;
    private boolean includeRelatedConcept;
}
