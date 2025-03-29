package com.capstone.domain.user.repository;

import com.capstone.domain.user.entity.User;
import com.capstone.domain.user.repository.custom.CustomUserRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String>, CustomUserRepository {
}
