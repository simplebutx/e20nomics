package com.htm.e20nomics.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTH_EXCEPTION_ATTRIBUTE = "auth_exception";
    public static final String INVALID_TOKEN_TYPE = "INVALID_TOKEN_TYPE";
    public static final String EXPIRED_TOKEN = "EXPIRED_TOKEN";
    public static final String INVALID_TOKEN = "INVALID_TOKEN";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    // 요청이 들어올때마다 실행하는 필터 로직
    // 요청에 토큰이 있으면 -> 검증하고 -> db에서 사용자 정보를 꺼내와 -> Authentication을 만들어서 -> SecurityContext에 넣는다
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        try {
            if (token != null) {
                jwtTokenProvider.validate(token);

                String email = jwtTokenProvider.getEmail(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);  // db에서 사용자 조회

                // Authentication 객체 생성
                // 서버는 로그인 상태를 기억하지 않으므로 매 요청마다 SecurityContext에 (db에서 방금 꺼내온) 유저정보를 저장해야함
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));   // Authentication 객체에 요청 관련 정보를 추가
                SecurityContextHolder.getContext().setAuthentication(authentication);  // 생성한 authentication을 security context에 집어넣어라
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute(AUTH_EXCEPTION_ATTRIBUTE, EXPIRED_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            request.setAttribute(AUTH_EXCEPTION_ATTRIBUTE, INVALID_TOKEN);
        }

        filterChain.doFilter(request, response);  // 다음 필터로 넘김
    }

    private String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || authHeader.isBlank()) {
            return null;
        }

        if (!authHeader.startsWith("Bearer ")) {
            request.setAttribute(AUTH_EXCEPTION_ATTRIBUTE, INVALID_TOKEN_TYPE);
            return null;
        }

        String token = authHeader.substring(7);
        if (token.isBlank()) {
            request.setAttribute(AUTH_EXCEPTION_ATTRIBUTE, INVALID_TOKEN);
            return null;
        }

        return token;
    }
}

//SecurityContextHolder
//      ↓
//SecurityContext
//      ↓
//Authentication   ← 현재 로그인 사용자 정보
//      ├ principal (사용자 정보)
//      ├ credentials (비밀번호)
//      └ authorities (권한)