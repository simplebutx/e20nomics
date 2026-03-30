package com.htm.e20nomics.auth.handler;

import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.auth.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtTokenProvider.createToken(user);

        response.sendRedirect( "http://localhost:5173/oauth-success?token=" + token);
    }
}