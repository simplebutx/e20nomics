package com.htm.e20nomics.todaynews.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TodayNewsResponse {
    private Long id;
    private String summaryTitle;
    private String summaryText;
    private LocalDateTime createdAt;
}
