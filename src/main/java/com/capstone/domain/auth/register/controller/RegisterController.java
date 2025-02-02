package com.capstone.domain.auth.register.controller;

import com.capstone.domain.auth.entity.User;
import com.capstone.domain.auth.register.dto.RegisterRequest;
import com.capstone.domain.auth.register.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {
    private final RegisterService registerService;

    @PostMapping("/new")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(registerService.registerUser(registerRequest));
    }
}
