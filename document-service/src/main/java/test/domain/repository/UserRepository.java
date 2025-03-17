package test.domain.repository;

import test.domain.entity.User;
import test.domain.repository.custom.CustomUserRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String>, CustomUserRepository {
}
