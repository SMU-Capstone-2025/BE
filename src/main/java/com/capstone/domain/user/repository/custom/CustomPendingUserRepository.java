package com.capstone.domain.user.repository.custom;

import com.capstone.domain.user.entity.PendingUser;

public interface CustomPendingUserRepository {
    PendingUser findByCredentialCode(String credentialCode);
}
