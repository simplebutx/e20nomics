package com.htm.e20nomics.term.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MyTermUpdateRequest {
    @NotBlank(message = "단어를 입력해 주세요.")
    private String term;

    @NotBlank(message = "정의를 입력해 주세요.")
    private String definition;


}
