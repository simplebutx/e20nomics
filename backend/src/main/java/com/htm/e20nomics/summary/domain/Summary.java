package com.htm.e20nomics.summary.domain;

import com.htm.e20nomics.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Summary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String originalText;

    private String summaryTitle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String summaryText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Enumerated(EnumType.STRING)
    private CreatedBy createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Summary(String originalText, String summaryTitle, String summaryText, User author, CreatedBy createdBy) {
        this.originalText = originalText;
        this.summaryTitle = summaryTitle;
        this.summaryText = summaryText;
        this.author = author;
        this.createdBy = createdBy;
    }
}
