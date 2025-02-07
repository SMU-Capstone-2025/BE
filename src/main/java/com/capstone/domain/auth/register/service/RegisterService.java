package com.capstone.domain.auth.register.service;

import com.capstone.domain.auth.register.dto.RegisterRequest;
import com.capstone.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.capstone.domain.user.entity.User.createUser;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public String registerUser(RegisterRequest registerRequest){
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(createUser(registerRequest));
        return "회원가입 성공";
    }

    public boolean checkEmail(String email){
        return userRepository.findUserByEmail(email) == null;
    }
}
