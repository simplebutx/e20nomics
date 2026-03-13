package com.htm.e20nomics.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MySummariesResponse {
    private Long id;
    private String summaryTitle;
    private String summaryText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
