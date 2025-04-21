package org.example.expert.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD) // 메서드에만 적용
@Retention(RetentionPolicy.RUNTIME) // 런타임에도 유지되어야 AOP에서 인식 가능
@Documented
public @interface AdminLog {
    String value() default ""; // 예: "게시글 삭제"
}