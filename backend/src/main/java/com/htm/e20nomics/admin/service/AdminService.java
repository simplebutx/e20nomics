package com.htm.e20nomics.admin.service;

import com.htm.e20nomics.admin.dto.AdminUserListResponse;
import com.htm.e20nomics.term.repository.MyTermRepository;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final MyTermRepository myTermRepository;

    @Transactional(readOnly = true)
    public List<AdminUserListResponse> getUserList() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(user -> new AdminUserListResponse
                        (user.getId(), user.getEmail(), user.getUserName(),
                                user.getRole(), user.getCreatedAt()))
                .toList();
    }
}
