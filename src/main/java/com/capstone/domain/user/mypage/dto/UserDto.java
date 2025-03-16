package com.capstone.domain.user.mypage.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserDto
{

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserPasswordDto
    {
        private String currentPassword;
        private String newPassword;
        private String confirmPassword;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserInfoDto
    {
        private String id;
        private String name;
        private String email;
        private String profileImage;
        private String social;
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserProfileDto
    {
        private String profileImage;
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserEmailDto
    {
        private String currentEmail;
        private String newEmail;
    }



}