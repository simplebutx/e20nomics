package com.htm.e20nomics.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SummaryCreateRequest {
    private String originalText;
    private String summaryTitle;
    private String summaryText;
    private String memo;
}
