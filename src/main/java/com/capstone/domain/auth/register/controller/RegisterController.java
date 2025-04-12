package com.capstone.domain.auth.register.controller;

import com.capstone.docs.RegisterControllerDocs;
import com.capstone.domain.auth.register.dto.RegisterRequest;
import com.capstone.domain.auth.register.service.RegisterService;
import com.capstone.domain.user.entity.User;
import com.capstone.global.mail.service.MailService;
import com.capstone.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController implements RegisterControllerDocs {
    private final RegisterService registerService;

    @PostMapping("/new")
    public ResponseEntity<ApiResponse<User>> registerUser(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(ApiResponse.onSuccess(registerService.registerUser(registerRequest)));
    }

    //true -> 사용 가능, false -> 사용 불가.
    @GetMapping("/avail-check")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailAvailable(@RequestParam String email){
        return ResponseEntity.ok(ApiResponse.onSuccess(registerService.checkEmail(email)));
    }

    @GetMapping("/mail-check")
    public ResponseEntity<ApiResponse<String>> sendMailConfirm(@RequestParam String email) throws Exception {
        return ResponseEntity.ok(ApiResponse.onSuccess(registerService.validateAndSendMail(email)));
    }

}
