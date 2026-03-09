package com.htm.e20nomics.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    // 요청이 들어올때마다 실행하는 필터 로직
    // 요청에 토큰이 있으면 -> 검증하고 -> db에서 사용자 정보를 꺼내와 -> Authentication을 만들어서 -> SecurityContext에 넣는다
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if(token != null) {
            boolean valid = jwtTokenProvider.validate(token);
        }
        if(token != null && jwtTokenProvider.validate(token)) {
            String email = jwtTokenProvider.getEmail(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);  // db에서 사용자 조회

            // Authentication 객체 생성
            // 서버는 로그인 상태를 기억하지 않으므로 매 요청마다 SecurityContext에 (db에서 방금 꺼내온) 유저정보를 저장해야함
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));   // Authentication 객체에 요청 관련 정보를 추가
            SecurityContextHolder.getContext().setAuthentication(authentication);    // 생성한 authentication을 security context에 집어넣어라
        }
        filterChain.doFilter(request, response);   // 다음 필터로 넘김
    }

    private String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        if(!authHeader.startsWith("Bearer ")) return null;
        return authHeader.substring(7);
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