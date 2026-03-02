package com.htm.e20nomics.briefing.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserBriefingCreateRequest {

    @NotBlank(message = "text는 필수입니다.")
    private String text;
}
