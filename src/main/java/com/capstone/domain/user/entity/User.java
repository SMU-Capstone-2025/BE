package com.capstone.domain.auth.entity;

import com.capstone.domain.auth.login.dto.LoginRequest;
import com.capstone.domain.auth.register.dto.RegisterRequest;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Document(collection = "user")

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private List<String> projectIds;

    public static User createUser(RegisterRequest registerRequest){
        return User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .build();
    }

    public User(String email){
        this.email = email;
    }
}
