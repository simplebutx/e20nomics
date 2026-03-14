package com.htm.e20nomics.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AnnouncementDetailResponse {

    private String summaryTitle;
    private String summaryText;
    private String authorName;
    private LocalDateTime createdAt;
}
