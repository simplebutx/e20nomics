package com.htm.e20nomics.summary.domain;

import com.htm.e20nomics.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @Column(columnDefinition = "TEXT", nullable = false)
    private String summaryText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    private boolean isPublic;

    @Enumerated(EnumType.STRING)
    private CreatedBy createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Summary(String originalText, String summaryText, User author, boolean isPublic, CreatedBy createdBy) {
        this.originalText = originalText;
        this.summaryText = summaryText;
        this.author = author;
        this.isPublic = isPublic;
        this.createdBy = createdBy;
    }
}
