package com.capstone.domain.auth.login.controller;

import com.capstone.domain.auth.login.dto.LoginRequest;
import com.capstone.global.jwt.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final CookieUtil cookieUtil;

    @PostMapping("/login")
    private ResponseEntity<String> doLogin(){
        return ResponseEntity.ok().build();
    }
}
