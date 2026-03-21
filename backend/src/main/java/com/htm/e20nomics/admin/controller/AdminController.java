package com.htm.e20nomics.admin.controller;

import com.htm.e20nomics.todaynews.dto.*;
import com.htm.e20nomics.admin.dto.AdminUserListResponse;
import com.htm.e20nomics.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    public final AdminService adminService;

    @GetMapping("/api/admin/users")
    public List<AdminUserListResponse> getUserList() {
        return adminService.getUserList();
    }
}
