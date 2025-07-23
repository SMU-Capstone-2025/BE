package com.capstone.domain.user.repository;

import com.capstone.domain.user.entity.PendingUser;
import com.capstone.domain.user.repository.custom.CustomPendingUserRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingUserRepository extends MongoRepository<PendingUser, String>, CustomPendingUserRepository {
}
