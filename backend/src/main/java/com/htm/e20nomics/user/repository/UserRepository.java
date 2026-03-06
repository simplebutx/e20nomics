package com.htm.e20nomics.user.repository;

import com.htm.e20nomics.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
