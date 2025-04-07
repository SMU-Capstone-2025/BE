package com.capstone.domain.auth.register.service;

import com.capstone.domain.auth.register.dto.RegisterRequest;
import com.capstone.domain.user.entity.User;
import com.capstone.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RegisterService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User registerUser(RegisterRequest registerRequest){
        return userRepository.save(registerRequest.from(passwordEncoder.encode(registerRequest.password())));
    }

    public boolean checkEmail(String email){
        return userRepository.findUserByEmail(email) == null;
    }
}
