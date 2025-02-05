package com.capstone.global.jwt;

import com.capstone.domain.auth.login.dto.LoginRequest;
import com.capstone.global.security.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, CookieUtil cookieUtil){
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        super.setAuthenticationManager(authenticationManager);
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("Attempting authentication...");
        try {
            String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            LoginRequest loginRequest = new ObjectMapper().readValue(messageBody, LoginRequest.class);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword(), null);

            return getAuthenticationManager().authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read request body", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication)
            throws IOException, ServletException {
        log.info("Authentication successful...");

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        try {
            response.addHeader("access", jwtUtil.createAccess(email));
        } catch (Exception e) {
            log.error("Error generating JWT: ", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        response.addCookie(cookieUtil.createCookie("refresh", jwtUtil.createRefresh(email)));
        response.setStatus(HttpStatus.OK.value());
    }

}
