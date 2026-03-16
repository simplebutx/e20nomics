package com.htm.e20nomics.summary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SummaryUpdateRequest {
    @NotBlank
    private String summaryTitle;
    @NotBlank
    private String summaryText;
    private String memo;
}
