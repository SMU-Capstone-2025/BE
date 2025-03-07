package domain.repository.custom;

import domain.entity.User;

public interface CustomUserRepository {
    User findUserByEmail(String email);
}
