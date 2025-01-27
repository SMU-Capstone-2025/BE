package com.capstone.domain.task.service;

import com.capstone.domain.task.dto.TaskDto;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;
import com.capstone.domain.task.exception.TaskNotFoundException;
import com.capstone.domain.task.message.TaskMessages;
import com.capstone.domain.task.repository.TaskRepository;
import com.capstone.domain.task.util.TaskUtil;
import com.capstone.global.elastic.entity.LogEntity;
import com.capstone.global.elastic.repository.LogRepository;
import com.capstone.global.kafka.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final LogRepository logRepository;
    private final KafkaProducerService kafkaProducerService;

    public String saveTask(TaskDto taskDto){
        taskRepository.save(TaskUtil.toEntity(taskDto));
        return TaskMessages.TASK_CREATED;
    }

    public Version loadVersionContent(String taskId) {
        Task task = findTaskByIdOrThrow(taskId);
        String currentVersion = task.getCurrentVersion();
        return taskRepository.findByTaskIdAndVersion(taskId, currentVersion);
    }

    public String addVersion(TaskDto taskDto){
        Version version = TaskUtil.createVersion(taskDto);
        Task task = findTaskByIdOrThrow(taskDto.getId());
        task.getVersionHistory().add(version);
        taskRepository.save(task);
        kafkaProducerService.sendTaskEvent("log-event", "ADD", taskDto, "pjy1121");
        return TaskMessages.VERSION_ADDED;
    }

    public String dropTask(String id){
        Task task = findTaskByIdOrThrow(id);
        taskRepository.delete(task);
        return TaskMessages.TASK_DROPPED;
    }

    public String updateStatus(String id, String status){
        Task task = findTaskByIdOrThrow(id);
        task.setStatus(status);
        taskRepository.save(task);
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
        return task;
    }

    public String modifyVersion(TaskDto taskDto) {
        return taskRepository.modifyVersion(taskDto);
    }

    public List<LogEntity> findLogsByTaskId(String taskId){
        return logRepository.findAllByTaskId(taskId);
    }

    public Task findTaskByIdOrThrow(String id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TaskMessages.TASK_NOT_FOUND));
    }
}
