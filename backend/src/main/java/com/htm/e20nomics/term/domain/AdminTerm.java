package com.htm.e20nomics.term.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

// 관리자 저장 단어 테이블
@Entity
@Getter
@NoArgsConstructor
public class AdminTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String term;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String definition;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public AdminTerm(String term, String definition) {
        this.term = term;
        this.definition = definition;
    }

    public void update(String term, String definition) {
        this.term = term;
        this.definition = definition;
    }
}