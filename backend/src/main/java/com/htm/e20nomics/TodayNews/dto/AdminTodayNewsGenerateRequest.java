package com.htm.e20nomics.todaynews.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminTodayNewsGenerateRequest {

    @NotBlank(message = "기사 내용을 입력하세요.")
    @Size(max = 3000, message = "3000자 이하로 입력해주세요.")
    String text;
}
