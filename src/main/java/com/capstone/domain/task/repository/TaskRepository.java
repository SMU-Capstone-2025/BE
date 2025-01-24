package com.capstone.domain.task.repository;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.repository.custom.CustomTaskRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task, String>, CustomTaskRepository {
}
