package com.capstone.domain.user.entity;

import com.capstone.domain.auth.register.dto.RegisterRequest;
import com.capstone.domain.user.mypage.dto.UserDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
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
    private String profileImage;
    // 기본 회원가입 시 null.
    private String social;
    private String password;
    private List<String> projectIds;

    public static User createUser(RegisterRequest registerRequest){
        return User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .projectIds(new ArrayList<>())
                .build();
    }
    public UserDto.UserInfoDto toDto() {
        return UserDto.UserInfoDto.builder()
                .name(name)
                .email(email)
                .profileImage(profileImage)
                .social(social)
                .build();
    }

    public User(String email){
        this.email = email;
    }
}
