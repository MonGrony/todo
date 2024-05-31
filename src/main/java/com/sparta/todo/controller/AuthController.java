package com.sparta.todo.controller;

import com.sparta.todo.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/create-jwt")
    public String createJwt(HttpServletResponse res) {
        String token = jwtUtil.createToken("Robbie", com.sparta.todo.entity.UserRoleType.USER);
        jwtUtil.addJwtToCookie(token, res);

        return "createJwt : " + token;
    }

    @GetMapping("/get-jwt")
    public String getJwt(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);

        // 토큰 검증
        if(!jwtUtil.validateToken(token)){
            throw new IllegalArgumentException("Token Error");
        }

        // 토큰에서 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);//token 에서 getBody 로 가져온 Claims 를 info 에 담음
        // 사용자 username 가져오기
        String username = info.getSubject();
        System.out.println("username = " + username);
        // 사용자 권한가져오기
        String authority = (String) info.get(JwtUtil.AUTHRORIZATION_KEY);
        System.out.println("authority = " + authority);

        return "getJwt : " + username + ", " + authority;
    }


}
