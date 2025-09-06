package com.capstone.domain.user.repository;

import com.capstone.domain.user.entity.User;
import com.capstone.domain.user.repository.custom.CustomUserRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String>, CustomUserRepository {
    List<User> findAllByEmailIn(Collection<String> emails);
}
