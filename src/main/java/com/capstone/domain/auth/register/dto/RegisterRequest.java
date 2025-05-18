package com.capstone.domain.auth.register.dto;

import com.capstone.domain.user.entity.MembershipType;
import com.capstone.domain.user.entity.Role;
import com.capstone.domain.user.entity.User;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Map;

@Builder
public record RegisterRequest(
    @NotBlank
    String name,
    @NotBlank
    String email,
    String password){

    public User from(String encodedPassword){
        return User.builder()
                .name(this.name)
                .email(this.email())
                .password(encodedPassword)
                .projectIds(new ArrayList<>())
                .membership(MembershipType.FREE_USER)
                .build();
    }

}

