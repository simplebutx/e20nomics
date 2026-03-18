package com.htm.e20nomics.user.repository;

import com.htm.e20nomics.user.domain.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    UserPreference findByUserId(Long UserId);
}
