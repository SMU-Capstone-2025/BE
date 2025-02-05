package com.capstone.domain.auth.repository.custom;

import com.capstone.domain.auth.entity.User;

public interface CustomUserRepository {
    User findUserByEmail(String email);
}
