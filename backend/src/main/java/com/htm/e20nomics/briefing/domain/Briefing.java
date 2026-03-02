package com.htm.e20nomics.briefing.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Briefing {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BriefingSourceType sourceType;

    @Lob // 이 칼럼에 긴 데이터를 저장할 것
    @Column(nullable = false)
    private String originalText;

    @Lob
    @Column(nullable = false)
    private String summaryText;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Briefing(BriefingSourceType sourceType, String originalText, String summaryText) {
        this.sourceType = sourceType;
        this.originalText = originalText;
        this.summaryText = summaryText;
        this.createdAt = LocalDateTime.now();
    }

}
