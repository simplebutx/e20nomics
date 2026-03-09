package com.htm.e20nomics.user.dto;


import com.htm.e20nomics.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MySummariesResponse {
    private Long id;
    private String originalText;
    private String summaryText;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
