package com.htm.e20nomics.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MySummaryUpdateRequest {
    @NotBlank
    private String summaryTitle;
    @NotBlank
    private String summaryText;
    private String memo;
}
