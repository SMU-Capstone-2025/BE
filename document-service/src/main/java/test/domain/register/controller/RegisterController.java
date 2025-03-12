package test.domain.register.controller;

import test.domain.register.dto.RegisterRequest;
import test.domain.register.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {
    private final RegisterService registerService;


    // TODO: 이 경로로 외부 요청을 보냈을 때 필터링 할 방법이 없어보임. 추가 필요.
    @PostMapping("/new")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(registerService.registerUser(registerRequest));
    }


}
