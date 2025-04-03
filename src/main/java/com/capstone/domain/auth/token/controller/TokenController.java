package com.capstone.domain.auth.token.controller;

import com.capstone.docs.TokenControllerDocs;
import com.capstone.global.jwt.CookieUtil;
import com.capstone.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController implements TokenControllerDocs {
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String accessToken, HttpServletRequest request) {
        return ResponseEntity.ok()
                .header("Set-Cookie", cookieUtil.createResponseCookie(jwtUtil.processToken(accessToken, request)).toString())
                .build();
    }
}
