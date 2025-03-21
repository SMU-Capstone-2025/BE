package com.capstone.domain.auth.login.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:63342")
public class LoginController {

    // 로그인 전 해당 경로로 요청을 보내 토큰 획득
    @GetMapping("/csrf-token")
    public String getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        return csrfToken != null ? csrfToken.getToken() : "CSRF token not found";
    }

    @PostMapping("/login")
    public ResponseEntity<String> doLogin(){
        return ResponseEntity.ok().build();
    }
}
