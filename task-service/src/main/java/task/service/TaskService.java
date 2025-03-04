package task.service;

import task.dto.TaskDto;
import task.entity.Task;
import task.entity.Version;
import task.event.TaskEventPublisher;
import task.exception.TaskNotFoundException;
import task.message.TaskMessages;
import task.repository.TaskRepository;
import task.util.TaskUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskUtil taskUtil;
    private final TaskEventPublisher taskEventPublisher;

    public String saveTask(TaskDto taskDto){
        taskRepository.save(taskUtil.toEntity(taskDto));
        return TaskMessages.TASK_CREATED;
    }

    public Version loadVersionContent(String taskId) {
        Task task = findTaskByIdOrThrow(taskId);
        String currentVersion = task.getCurrentVersion();
        return taskRepository.findByTaskIdAndVersion(taskId, currentVersion);
    }

    public String saveVersion(TaskDto taskDto, String fileId){
        Version version = taskUtil.createOrGetVersion(taskDto, fileId);
        Task task = findTaskByIdOrThrow(taskDto.getId());
        task.getVersionHistory().add(version);
        taskRepository.save(task);

        taskEventPublisher.publishVersionAdded(taskDto);
        return TaskMessages.VERSION_ADDED;
    }

    public String dropTask(String id){
        Task task = findTaskByIdOrThrow(id);
        taskRepository.delete(task);

        taskEventPublisher.publishTaskDeleted(task.getId());
        return TaskMessages.TASK_DROPPED;
    }

    public String updateStatus(String id, String status){
        Task task = findTaskByIdOrThrow(id);
        task.setStatus(status);
        taskRepository.save(task);

        taskEventPublisher.publishStatusUpdated(id, status);
        return TaskMessages.STATUS_UPDATED;
    }

    public List<Version> listVersions(String taskId){
        Task task = findTaskByIdOrThrow(taskId);
        return task.getVersionHistory();
    }

    public Task rollbackVersion(String taskId, String version){
        Task task = findTaskByIdOrThrow(taskId);
        task.setCurrentVersion(version);
        taskRepository.save(task);

        taskEventPublisher.publishVersionRollback(taskId, version);
        return task;
    }


    public Task findTaskByIdOrThrow(String id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TaskMessages.TASK_NOT_FOUND));
    }
}
