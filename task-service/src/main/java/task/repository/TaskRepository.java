package task.repository;
import task.entity.Task;
import task.repository.custom.CustomTaskRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task, String>, CustomTaskRepository {
}
