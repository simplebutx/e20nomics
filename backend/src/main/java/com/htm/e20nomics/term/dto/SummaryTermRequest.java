package com.htm.e20nomics.term.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 관리자가 오늘의 기사 등록할때 같이 보내는 단어
@Getter
@NoArgsConstructor
public class SummaryTermRequest {
    @NotBlank
    private String term;

    @NotBlank
    private String definition;
}