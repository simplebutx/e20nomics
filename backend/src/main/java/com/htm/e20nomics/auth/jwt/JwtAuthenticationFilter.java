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

        System.out.println("=== JWT FILTER START ===");
        System.out.println("URI = " + request.getRequestURI());
        System.out.println("method = " + request.getMethod());
        System.out.println("token exists = " + (token != null));

        try {
            if (token != null) {
                boolean valid = jwtTokenProvider.validate(token);
                System.out.println("token valid = " + valid);

                if (valid) {
                    String email = jwtTokenProvider.getEmail(token);
                    System.out.println("token subject/email = " + email);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    System.out.println("loaded userDetails = " + userDetails);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("authentication set = " + SecurityContextHolder.getContext().getAuthentication());
                }
            }
        } catch (Exception e) {
            System.out.println("JWT filter exception = " + e.getClass().getName());
            System.out.println("JWT filter message = " + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
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