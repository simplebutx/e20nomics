package com.htm.e20nomics.auth.domain;

import com.htm.e20nomics.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// 현재 로그인한 유저 정보 객체
public class CustomUserDetails implements UserDetails{

    private final User user;    // CustomUserDetailService에서 찾아서 받아온 그 user

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public Long getUserId() {return user.getId();}

    public String getName() {
        return user.getUserName();
    }

    public String getDisplayName() {
        return user.getDisplayName();
    }

    public String getRole() {return user.getRole().name(); }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
