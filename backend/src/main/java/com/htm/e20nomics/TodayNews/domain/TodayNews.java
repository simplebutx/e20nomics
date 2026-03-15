package com.htm.e20nomics.TodayNews.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class TodayNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String originalText;

    private String summaryTitle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String summaryText;

    private Boolean isPublished;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime publishedAt;

    public TodayNews(String originalText, String summaryTitle, String summaryText) {
        this.originalText = originalText;
        this.summaryTitle = summaryTitle;
        this.summaryText = summaryText;
        this.isPublished = false;
    }

    public void update(String summaryTitle, String summaryText, Boolean isPublished) {
        this.summaryTitle = summaryTitle;
        this.summaryText = summaryText;
        this.isPublished = isPublished;
    }
}
