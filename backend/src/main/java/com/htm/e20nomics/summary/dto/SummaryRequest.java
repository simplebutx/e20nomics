package com.htm.e20nomics.summary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SummaryRequest(

        @NotBlank(message = "기사 내용을 입력하세요.")
        @Size(max = 3000, message = "3000자 이하로 입력해주세요.")
        String text
) {
}
