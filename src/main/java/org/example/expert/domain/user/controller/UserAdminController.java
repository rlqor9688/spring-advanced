package org.example.expert.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.common.annotation.AdminLog;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.service.UserAdminService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserAdminController {

    private final UserAdminService userAdminService;

    @AdminLog("유저 권한 변경")
    @PatchMapping("/admin/users/{userId}")
    public void changeUserRole(@PathVariable long userId,
                               @RequestBody UserRoleChangeRequest userRoleChangeRequest,
                               @Auth AuthUser authUser)
    {
        log.info("관리자 요청자 ID: {}, 이메일: {}, 역할: {}", authUser.getId(), authUser.getEmail(), authUser.getUserRole());

        userAdminService.changeUserRole(userId, userRoleChangeRequest);
    }
}
