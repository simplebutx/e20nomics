package com.htm.e20nomics.auth.service;

import com.htm.e20nomics.auth.domain.CustomUserDetails;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    // JWT 방식에서도 authenticationManager.authenticate() 하면 이거 함수 호출함
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // JWT 방식에서도 토큰만 믿는것이 아니라 db에 조회해보는 과정이 필요
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("유저를 찾을수 없습니다."));

        return new CustomUserDetails(user);
    }
}