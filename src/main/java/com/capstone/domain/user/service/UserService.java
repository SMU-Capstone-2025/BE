package com.capstone.domain.user.service;

import com.capstone.domain.user.entity.User;
import com.capstone.domain.user.exception.UserNotFoundException;
import com.capstone.domain.user.message.UserMessages;
import com.capstone.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public void participateProcess(List<String> emails, String projectId) {
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, User.class);

        for (String email : emails) {
            User user = findUserByEmailOrThrow(email);

            List<String> updatedProjectIds = participateProject(user.getProjectIds(), projectId);

            Query query = new Query(Criteria.where("email").is(email));
            Update update = new Update().set("projectIds", updatedProjectIds);

            bulkOps.updateOne(query, update);
        }

        bulkOps.execute();
    }

    public List<String> participateProject(List<String> projectIds, String projectId) {
        if (projectIds == null) {
            projectIds = new ArrayList<>();
        }
        projectIds.add(projectId);
        return projectIds;
    }


    public User findUserByEmailOrThrow(String email){
        return Optional.ofNullable(userRepository.findUserByEmail(email))
                .orElseThrow(() -> new UserNotFoundException(UserMessages.USER_NOT_FOUND));
    }

}
