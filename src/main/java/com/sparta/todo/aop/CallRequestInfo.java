package com.sparta.todo.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CallRequestInfo {

    //- 모든 API(Controller)가 호출될 때, Request 정보(Request URL, HTTP Method)를
    //    **@Slf4J Logback** 라이브러리를  활용하여 Log로 출력해주세요.
    //    - 컨트롤러 마다 로그를 출력하는 코드를 추가하는것이 아닌, AOP로 구현해야만 합니다.

    @Pointcut("execution(* com.sparta.todo.controller.AuthController.*(..))")
    private void auth() {
    }

    @Pointcut("execution(* com.sparta.todo.controller.CommentController.*(..))")
    private void comment() {
    }

    @Pointcut("execution(* com.sparta.todo.controller.HomeController.*(..))")
    private void home() {
    }

    @Pointcut("execution(* com.sparta.todo.controller.TodoController.*(..))")
    private void todo() {
    }

    @Pointcut("execution(* com.sparta.todo.controller.UserController.*(..))")
    private void user() {
    }

    @Before("auth() || comment() || home() || todo() || user()")
    public String logBeforeAPI(JoinPoint joinPoint) { //proceeding~ 은 around 에서만
        log.info(" Before 작동");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("URL:{}, Method: {}", request.getRequestURL(), request.getMethod());

        log.info("Class: {}, Method: {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        return "요청하신 API를 시작합니다.";
    }

    @After("auth() || comment() || home() || todo() || user()")
    public String logAfterAPI(JoinPoint joinPoint) {
        log.info(" After 작동");
        return "API를 종료합니다.";
    }


}
