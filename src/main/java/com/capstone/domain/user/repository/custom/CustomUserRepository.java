package com.capstone.domain.user.repository.custom;

import com.capstone.domain.user.entity.User;

public interface CustomUserRepository {
    User findUserByEmail(String email);
}
