package com.capstone.domain.auth.register.controller;

import com.capstone.domain.auth.register.dto.RegisterRequest;
import com.capstone.domain.auth.register.service.RegisterService;
import com.capstone.global.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {
    private final RegisterService registerService;
    private final MailService mailService;


    // TODO: 이 경로로 외부 요청을 보냈을 때 필터링 할 방법이 없어보임. 추가 필요.
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
