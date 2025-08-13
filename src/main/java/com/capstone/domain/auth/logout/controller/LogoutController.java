package com.capstone.domain.auth.logout.controller;

import com.capstone.docs.LogoutControllerDocs;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LogoutController implements LogoutControllerDocs {

    @PostMapping("/logout")
    public ResponseEntity<String> doLogout(){
        return ResponseEntity.ok().build();
    }
}
