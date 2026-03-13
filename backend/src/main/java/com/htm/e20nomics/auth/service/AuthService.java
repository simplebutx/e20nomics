package com.htm.e20nomics.auth.service;

import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.auth.dto.LoginRequest;
import com.htm.e20nomics.auth.dto.SignupRequest;
import com.htm.e20nomics.auth.dto.TokenResponse;
import com.htm.e20nomics.auth.jwt.JwtTokenProvider;
import com.htm.e20nomics.global.exception.DuplicateEmailException;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(SignupRequest dto) {
        if(userRepository.existsByEmail(dto.getEmail()))
        {
            throw new DuplicateEmailException();
        }
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        User user = new User(dto.getEmail(), hashedPassword, dto.getUserName(), dto.getDisplayName());
        userRepository.save(user);
    }

    @Transactional
    // 이메일, 비번을 받아서 Spring Security로 인증하고, 성공하면 JWT 토큰을 만들어서 반환
    public TokenResponse login(LoginRequest dto) {
        // authenticationManager.authenticate() -> UserDetailsService.loadUserByUsername() -> CustomUserDetails 생성 -> PasswordEncoder.matches() -> Authentication 생성
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
                // UsernamePasswordAuthenticationToken: "이 이메일/비번으로 인증해줘"라는 요청 객체
                // authenticationManager.authenticate(): db에서 유저 조회, 일치하면 Authentication 객체 반환
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // (Spring Security가 방금 만든) Authentication 안에있는 로그인 성공한 사용자 정보를 꺼내서 (CustomUserDetails 타입으로 다운캐스팅)

        String token = jwtTokenProvider.createToken(userDetails);

        return new TokenResponse(token);   // dto로 변환
    }



//    Authentication
//     ├ principal      → 로그인 사용자 정보
//     ├ credentials    → password
//     └ authorities    → 권한
}
