package com.htm.e20nomics.todaynews.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminTodayNewsGenerateResponse {
    private String summaryTitle;
    private String summaryText;
    private boolean canSave;
}
