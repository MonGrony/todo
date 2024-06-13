package com.sparta.todo.controller;

import com.sparta.todo.dto.AuthRequestDto;
import com.sparta.todo.entity.UserRoleType;
import com.sparta.todo.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

//    //API -> 토큰 발급, 토큰 검증 (로그인이 있다고 가정하고 진행) //security 거치지 않고 할 때
//
//    @GetMapping("/authenticate")
//    public String createToken (@RequestBody AuthRequest authRequest) throws Exception {
//        if ("user".equals(authRequest.getUsername()) && "password".equals(authRequest.getPassword())) {
//            return JwtTokenProvider.generateToken(authRequest.getUsername()); //provider 가 곧 JwtUtil
//        } else {
//            throw new Exception("유효하지 않은 자격증명");
//        }
//    }
//
//    @GetMapping("/validate")
//    public String validateToken(@RequestBody("Authorization" String token) {
//        String username = JwtTokenProvider.extractUsername(token.substring(7));
//        if (JwtTokenProvider.validateToken(token.substring(7), username)) {
//            return "유효한 토큰";
//        } else {
//            return "유효하지 않은 토큰";
//        }
//    }

    @GetMapping("/create-jwt") //토큰이 String 타입으로 발급되서 Cookie 에 저장됨
    public void createJwt(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse res) {
        String token = jwtUtil.createToken(authRequestDto.getUsername(),
                authRequestDto.getUserRoleType());
        jwtUtil.addJwtToCookie(token, res);
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
        // 사용자 권한가져오기
        String authority = (String) info.get(JwtUtil.AUTHRORIZATION_KEY);

        return "getJwt : " + username + ", " + authority;
    }




}
