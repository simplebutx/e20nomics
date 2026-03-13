package com.htm.e20nomics.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SummaryGenerateResponse {
    private String summaryTitle;
    private String summaryText;
    private boolean canSave;
}
