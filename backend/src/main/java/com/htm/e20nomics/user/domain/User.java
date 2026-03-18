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
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime createdAt;

    public User(String email, String password, String userName) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.role = Role.USER;
        this.createdAt = LocalDateTime.now();
    }
}
