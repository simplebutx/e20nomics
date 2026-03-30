package com.htm.e20nomics.auth.service;

import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request)
            throws OAuth2AuthenticationException {

        OAuth2User oauthUser = super.loadUser(request);

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException("Google account email not found");
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() ->
                        userRepository.save(
                                new User(email, "OAUTH2", name != null ? name : "GoogleUser")
                        )
                );

        return new CustomUserDetails(user, oauthUser.getAttributes());
    }


}
