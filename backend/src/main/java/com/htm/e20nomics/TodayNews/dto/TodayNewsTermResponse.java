package com.htm.e20nomics.TodayNews.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodayNewsTermResponse {
    private Long termId;
    private String term;
    private String definition;
}
