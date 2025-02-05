package com.capstone.domain.auth.repository;

import com.capstone.domain.auth.entity.User;
import com.capstone.domain.auth.repository.custom.CustomUserRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String>, CustomUserRepository {
}
