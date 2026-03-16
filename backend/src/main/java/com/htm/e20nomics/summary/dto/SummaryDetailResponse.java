package com.htm.e20nomics.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SummaryDetailResponse {
    private String summaryTitle;
    private String summaryText;
    private String memo;
    private LocalDateTime createdAt;
}
