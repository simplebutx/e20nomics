package com.htm.e20nomics.term.domain;



import com.htm.e20nomics.summary.domain.Summary;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 오늘의 뉴스와 같이 내려오는 단어
@Entity
@NoArgsConstructor
@Getter
public class SummaryTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String term;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String definition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "summary_id", nullable = false)
    private Summary summary;

    public SummaryTerm(String term, String definition, Summary summary) {
        this.term = term;
        this.definition = definition;
        this.summary = summary;
    }
}