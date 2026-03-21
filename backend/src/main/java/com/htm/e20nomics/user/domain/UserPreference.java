package com.htm.e20nomics.user.domain;

import com.htm.e20nomics.user.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    private SummaryLength summaryLength;

    @Enumerated(EnumType.STRING)
    private SummaryDifficulty summaryDifficulty;

    @Enumerated(EnumType.STRING)
    private SummaryFormat summaryFormat;

    @Enumerated(EnumType.STRING)
    private SummaryExplainStyle summaryExplainStyle;


    @Enumerated(EnumType.STRING)
    private TermLength termLength;

    @Enumerated(EnumType.STRING)
    private TermDifficulty termDifficulty;

    private boolean includeExample;
    private boolean includeRelatedConcept;

    public UserPreference(User user, SummaryLength summaryLength, SummaryDifficulty summaryDifficulty,
                          SummaryFormat summaryFormat, SummaryExplainStyle summaryExplainStyle, TermLength termLength, TermDifficulty termDifficulty,
                          boolean includeExample, boolean includeRelatedConcept) {
        this.user = user;
        this.summaryLength = summaryLength;
        this.summaryDifficulty = summaryDifficulty;
        this.summaryFormat = summaryFormat;
        this.summaryExplainStyle = summaryExplainStyle;
        this.termLength = termLength;
        this.termDifficulty = termDifficulty;
        this.includeExample = includeExample;
        this.includeRelatedConcept = includeRelatedConcept;
    }

    public void update(
            SummaryLength summaryLength,
            SummaryDifficulty summaryDifficulty,
            SummaryFormat summaryFormat,
            SummaryExplainStyle summaryExplainStyle,
            TermLength termLength,
            TermDifficulty termDifficulty,
            boolean includeExample,
            boolean includeRelatedConcept
    ) {
        this.summaryLength = summaryLength;
        this.summaryDifficulty = summaryDifficulty;
        this.summaryFormat = summaryFormat;
        this.summaryExplainStyle = summaryExplainStyle;
        this.termLength = termLength;
        this.termDifficulty = termDifficulty;
        this.includeExample = includeExample;
        this.includeRelatedConcept = includeRelatedConcept;
    }
}
