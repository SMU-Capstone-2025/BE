package domain.register.service;

import domain.register.dto.RegisterRequest;
import domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static domain.entity.User.createUser;


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
