package com.capstone.domain.user.entity;

import com.capstone.domain.auth.register.dto.RegisterRequest;
import lombok.*;
import org.apache.lucene.spatial3d.geom.Membership;
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

    //멤버 멤버쉽
    private MembershipType membership;


    public static User createUser(RegisterRequest registerRequest){
        return User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .projectIds(new ArrayList<>())
                .membership(MembershipType.FREE_USER)
                .build();
    }

    public User(String email){
        this.email = email;
    }
}
