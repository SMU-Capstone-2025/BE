package com.capstone.domain.user.repository.custom;

import com.capstone.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface CustomUserRepository {
    Optional<User> findUserByEmail(String email);
    List<String> findUserProjectByEmail(String email);
}
