package com.htm.e20nomics.user.domain;


import com.htm.e20nomics.auth.domain.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String userName;
    private String displayName;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime createdAt;
    private String summaryLevel;
    private String summaryStyle;
    private String summaryLength;

    public User(String email, String password, String userName, String displayName) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.displayName = displayName;
        this.role = Role.USER;
        this.createdAt = LocalDateTime.now();
    }
}
