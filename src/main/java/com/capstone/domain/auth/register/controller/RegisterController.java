package com.capstone.domain.auth.register.controller;

import com.capstone.domain.auth.entity.User;
import com.capstone.domain.auth.register.dto.RegisterRequest;
import com.capstone.domain.auth.register.service.RegisterService;
import com.capstone.global.mail.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {
    private final RegisterService registerService;
    private final MailService mailService;

    @PostMapping("/new")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(registerService.registerUser(registerRequest));
    }

    //true -> 사용 가능, false -> 사용 불가.
    @GetMapping("/avail-check")
    public ResponseEntity<Boolean> checkEmailAvailable(@RequestParam String email){
        return ResponseEntity.ok(registerService.checkEmail(email));
    }

    @PostMapping("/mail-check")
    public ResponseEntity<String> sendMailConfirm(@RequestParam String email) throws Exception {
        return ResponseEntity.ok(mailService.sendSimpleMessage(email));
    }

}
