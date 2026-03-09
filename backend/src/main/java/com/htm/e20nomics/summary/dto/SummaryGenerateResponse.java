package com.htm.e20nomics.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SummaryGenerateResponse {
    private String summary;
    private boolean canSave;
}
