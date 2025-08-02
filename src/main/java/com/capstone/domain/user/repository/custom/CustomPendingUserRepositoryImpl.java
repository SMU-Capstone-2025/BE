package com.capstone.domain.user.repository.custom;

import com.capstone.domain.user.entity.PendingUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomPendingUserRepositoryImpl implements CustomPendingUserRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public PendingUser findByCredentialCode(String credentialCode) {
        Query query = new Query().addCriteria(Criteria.where("credentialCode").is(credentialCode));

        return mongoTemplate.findOne(query, PendingUser.class);
    }

    @Override
    public Optional<PendingUser> findByProjectAndUser(String projectId, String userId) {
        Query query = new Query().addCriteria(Criteria.where("projectId").is(projectId)
                .and("userId").is(userId));

        return Optional.ofNullable(mongoTemplate.findOne(query, PendingUser.class));
    }
}
