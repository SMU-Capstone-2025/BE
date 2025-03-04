package task.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import task.dto.TaskDto;

@Service
public class TaskEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TaskEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishTaskCreated(TaskDto taskDto) {
        kafkaTemplate.send("task-events", "TASK_CREATED", taskDto);
    }

    public void publishVersionAdded(TaskDto taskDto) {
        kafkaTemplate.send("task-events", "VERSION_ADDED", taskDto);
    }

    public void publishTaskDeleted(String taskId) {
        kafkaTemplate.send("task-events", "TASK_DELETED", taskId);
    }

    public void publishStatusUpdated(String taskId, String status) {
        kafkaTemplate.send("task-events", "STATUS_UPDATED", taskId + ":" + status);
    }

    public void publishVersionRollback(String taskId, String version) {
        kafkaTemplate.send("task-events", "VERSION_ROLLBACK", taskId + ":" + version);
    }

    public void publishLogFind(String taskId) {
        kafkaTemplate.send("log-events", "LOG_FIND", taskId);
    }
}