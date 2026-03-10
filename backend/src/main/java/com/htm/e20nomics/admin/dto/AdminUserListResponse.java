package com.htm.e20nomics.admin.dto;

import com.htm.e20nomics.auth.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminUserListResponse {

    private Long id;
    private String email;
    private String userName;
    private String displayName;
    private Role role;
    private LocalDateTime createdAt;
}
