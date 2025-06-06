package com.capstone.domain.user.repository.custom;

import com.capstone.domain.user.entity.User;

import java.util.List;

public interface CustomUserRepository {
    User findUserByEmail(String email);
    List<String> findUserProjectByEmail(String email);
}
