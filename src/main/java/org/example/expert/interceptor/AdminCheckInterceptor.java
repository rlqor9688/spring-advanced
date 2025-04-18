package org.example.expert.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.example.expert.config.JwtUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminCheckInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
            return false;
        }

        try {
            String token = jwtUtil.substringToken(authHeader);
            UserRole userRole = jwtUtil.getUserRole(token);

            if (userRole != UserRole.ADMIN) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다.");
                return false;
            }

            // 요청 시각 + URI 로깅
            String requestTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String requestURI = request.getRequestURI();
            log.info("[ADMIN ACCESS] 시간: {}, 요청 URL: {}", requestTime, requestURI);

            // 사용자 정보 request에 담기
            request.setAttribute("userId", jwtUtil.getUserId(token));
            request.setAttribute("userEmail", jwtUtil.getUserEmail(token));
            request.setAttribute("userRole", userRole);

            return true;

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 유효하지 않습니다.");
            return false;
        }

    }
}
