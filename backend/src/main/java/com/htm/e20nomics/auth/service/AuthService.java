package com.htm.e20nomics.auth.service;

import com.htm.e20nomics.auth.dto.SignupRequest;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequest dto) {
        // 이메일 중복검사

        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        User user = new User(dto.getEmail(), hashedPassword, dto.getUserName(), dto.getDisplayName());
        userRepository.save(user);
    }
}
