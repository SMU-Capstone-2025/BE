package test.domain.register.service;

import test.domain.register.dto.RegisterRequest;
import test.domain.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static test.domain.entity.User.createUser;


@Service
public class RegisterService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RegisterService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public String registerUser(RegisterRequest registerRequest){
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(createUser(registerRequest));
        return "회원가입 성공";
    }

    public boolean checkEmail(String email){
        return userRepository.findUserByEmail(email) == null;
    }
}
