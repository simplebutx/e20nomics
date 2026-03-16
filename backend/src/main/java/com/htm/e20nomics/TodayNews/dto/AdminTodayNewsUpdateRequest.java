package com.htm.e20nomics.TodayNews.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminTodayNewsUpdateRequest {
    @NotBlank(message = "제목은 비어 있을 수 없습니다.")
    private String summaryTitle;

    @NotBlank(message = "요약문은 비어 있을 수 없습니다.")
    private String summaryText;
    private Boolean isPublished;
}