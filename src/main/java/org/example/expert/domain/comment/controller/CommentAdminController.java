package org.example.expert.domain.comment.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.comment.service.CommentAdminService;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentAdminController {

    private final CommentAdminService commentAdminService;

    @DeleteMapping("/admin/comments/{commentId}")
    public void deleteComment(@PathVariable long commentId, HttpServletRequest request) {
        Long requesterId = (Long) request.getAttribute("userId");
        String email = (String) request.getAttribute("userEmail");
        UserRole role = (UserRole) request.getAttribute("userRole");

        log.info("관리자 요청자 ID: {}, 이메일: {}, 역할: {}", requesterId, email, role);

        commentAdminService.deleteComment(commentId);
    }
}
