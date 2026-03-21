package com.htm.e20nomics.todaynews.domain;

import com.htm.e20nomics.term.domain.AdminTerm;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


// 중간 연결 테이블 뉴스:단어 (m:n)
@Entity
@Getter
@NoArgsConstructor
public class TodayNewsTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "today_news_id", nullable = false)
    private TodayNews todayNews;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_term_id", nullable = false)
    private AdminTerm adminTerm;

    public TodayNewsTerm(TodayNews todayNews, AdminTerm adminTerm) {
        this.todayNews = todayNews;
        this.adminTerm = adminTerm;
    }
}
