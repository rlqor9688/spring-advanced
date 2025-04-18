package org.example.expert.common.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.expert.common.annotation.AdminLog;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AdminLogAspect {

    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Around("@annotation(org.example.expert.common.annotation.AdminLog)")
    public Object logAdminApi(ProceedingJoinPoint joinPoint) throws Throwable {
        String token = jwtUtil.substringToken(request.getHeader("Authorization"));
        Long userId = jwtUtil.getUserId(token);

        String requestTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String uri = request.getRequestURI();
        String httpMethod = request.getMethod();
        String requestBody = argsToJson(joinPoint.getArgs());

        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed(); // 메서드 실행
            long end = System.currentTimeMillis();

            String responseBody = objectMapper.writeValueAsString(result);

            // ======== 어노테이션 값 읽기 ========
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method targetMethod = signature.getMethod();
            AdminLog adminLogAnnotation = targetMethod.getAnnotation(AdminLog.class);
            String actionDescription = adminLogAnnotation.value();

            log.info("""
                [관리자 API 호출 로그]
                - 행위: {}
                - 사용자 ID: {}
                - 시간: {}
                - URI: {} ({})
                - 요청 본문: {}
                - 응답 본문: {}
                - 처리 시간: {}ms
                """,
                actionDescription, userId, requestTime, uri, httpMethod, requestBody, responseBody, (end-start));
            return result;
        } catch (Throwable e) {
            log.error("관리자 API 예외 - 사용자 ID: {}, URI: {}, 오류: {}", userId, uri, e.getMessage());
            throw e;
        }
    }

    private String argsToJson(Object[] args) {
        try {
            return objectMapper.writeValueAsString(args);
        } catch (JsonProcessingException e) {
            return "요청 파라미터 JSON 변환 실패";
        }
    }
}
