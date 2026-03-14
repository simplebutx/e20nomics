package com.htm.e20nomics.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyTermDetailResponse {
    private String term;
    private String definition;
    private LocalDateTime createdAt;
}
