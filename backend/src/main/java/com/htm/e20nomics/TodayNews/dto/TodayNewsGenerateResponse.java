package com.htm.e20nomics.TodayNews.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodayNewsGenerateResponse {
    private String summaryTitle;
    private String summaryText;
    private boolean canSave;
}
