package com.htm.e20nomics.summary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SummaryCreateRequest {
    @NotBlank
    private String originalText;
    @NotBlank
    private String summaryTitle;
    @NotBlank
    private String summaryText;
    private String memo;
}
