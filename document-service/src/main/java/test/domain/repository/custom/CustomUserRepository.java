package test.domain.repository.custom;

import test.domain.entity.User;

public interface CustomUserRepository {
    User findUserByEmail(String email);
}
