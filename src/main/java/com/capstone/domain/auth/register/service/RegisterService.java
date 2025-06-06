package com.capstone.domain.auth.register.service;

import com.capstone.domain.auth.register.dto.RegisterRequest;
import com.capstone.domain.user.entity.User;
import com.capstone.domain.user.repository.UserRepository;
import com.capstone.global.mail.service.MailService;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RegisterService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;

    public User registerUser(RegisterRequest registerRequest){
        return userRepository.save(registerRequest.from(passwordEncoder.encode(registerRequest.password())));
    }

    public boolean checkEmail(String email){
        if (userRepository.findUserByEmail(email) != null){
            System.out.println(email);
            throw new GlobalException(ErrorStatus.USER_ALREADY_EXISTS);
        }
        return true;
    }

    public String validateAndSendMail(String email) throws Exception {
        if (userRepository.findUserByEmail(email) != null){
            throw new GlobalException(ErrorStatus.USER_ALREADY_EXISTS);
        }
        return mailService.sendSimpleMessage(email);
    }
}
