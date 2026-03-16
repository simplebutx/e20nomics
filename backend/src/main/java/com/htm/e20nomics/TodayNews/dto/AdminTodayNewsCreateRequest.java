package com.htm.e20nomics.TodayNews.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminTodayNewsCreateRequest {
    @NotBlank
    private String originalText;
    @NotBlank
    private String summaryTitle;
    @NotBlank
    private String summaryText;
}
