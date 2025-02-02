package com.capstone.global.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
public class CookieUtil {
    private static final int COOKIE_EXPIRE_TIME = 30 * 60; // 30분

    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_EXPIRE_TIME);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);  // HTTPS 요청에만 secure 설정
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }

    public Cookie createCsrfCookie(HttpServletRequest request, HttpServletResponse response){
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        Cookie csrfCookie = new Cookie("XSRF-TOKEN", csrfToken.getToken());
        csrfCookie.setSecure(false);
        csrfCookie.setPath("/");
        response.addCookie(csrfCookie);

        return csrfCookie;
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Optional.ofNullable(request.getCookies())
                .ifPresent(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> name.equals(cookie.getName()))
                        .forEach(cookie -> {
                            cookie.setValue("");  // 필요하지 않음, setMaxAge(0)으로 충분함
                            cookie.setPath("/");
                            cookie.setMaxAge(0);  // 쿠키 삭제
                            cookie.setHttpOnly(true);
                            cookie.setSecure(request.isSecure());
                            response.addCookie(cookie);
                        }));
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        return Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> c.getName().equals(name))
                .map(Cookie::getValue)
                .findAny().orElse(null);
    }
}
