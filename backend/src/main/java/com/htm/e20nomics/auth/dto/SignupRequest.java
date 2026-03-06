package com.htm.e20nomics.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequest {
    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    @NotBlank(message = "이름을 입력하세요.")
    private String userName;

    @NotBlank(message = "닉네임(활동명)을 입력하세요.")
    private String displayName;
}
