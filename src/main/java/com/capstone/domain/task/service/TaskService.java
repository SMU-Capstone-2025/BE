package com.capstone.domain.task.service;

import com.capstone.domain.task.dto.TaskDto;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;
import com.capstone.domain.task.exception.TaskNotFoundException;
import com.capstone.domain.task.message.TaskMessages;
import com.capstone.domain.task.repository.TaskRepository;
import com.capstone.domain.task.util.TaskUtil;
import com.capstone.domain.log.entity.LogEntity;
import com.capstone.domain.log.repository.LogRepository;
import com.capstone.global.jwt.JwtUtil;
import com.capstone.global.kafka.service.KafkaProducerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final LogRepository logRepository;
    private final KafkaProducerService kafkaProducerService;
    private final TaskUtil taskUtil;
    private final JwtUtil jwtUtil;

    @Transactional
    public String saveTask(TaskDto taskDto){
        taskRepository.save(taskDto.toTask());
        return TaskMessages.TASK_CREATED;
    }

    public Version loadVersionContent(String taskId) {
        Task task = findTaskByIdOrThrow(taskId);
        String currentVersion = task.getCurrentVersion();
        return taskRepository.findByTaskIdAndVersion(taskId, currentVersion);
    }

    @Transactional
    public String saveVersion(TaskDto taskDto, String fileId, String token){
        Version version = taskUtil.createOrGetVersion(taskDto, fileId);
        Task task = findTaskByIdOrThrow(taskDto.id());
        task.addNewVersion(version);
        taskRepository.save(task);

        kafkaProducerService.sendTaskEvent("task.changed", "ADD", taskDto, jwtUtil.getEmail(token));
        return TaskMessages.VERSION_ADDED;
    }

    @Transactional
    public String dropTask(String id){
        Task task = findTaskByIdOrThrow(id);
        taskRepository.delete(task);
        return TaskMessages.TASK_DROPPED;
    }

    @Transactional
    public String updateStatus(String id, String status, String token){
        Task task = findTaskByIdOrThrow(id);
        task.updateStatus(status);
        taskRepository.save(task);
        kafkaProducerService.sendTaskEvent("task.updated", "UPDATE", task, jwtUtil.getEmail(token));

        return TaskMessages.STATUS_UPDATED;
    }

    public List<Version> listVersions(String taskId){
        Task task = findTaskByIdOrThrow(taskId);
        return task.getVersionHistory();
    }

    @Transactional
    public Task rollbackVersion(String taskId, String version){
        Task task = findTaskByIdOrThrow(taskId);
        task.updateCurrentVersion(version);
        taskRepository.save(task);
        return task;
    }


    public List<LogEntity> findLogsByTaskId(String taskId){
        return logRepository.findAllByTaskId(taskId);
    }

    public Task findTaskByIdOrThrow(String id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TaskMessages.TASK_NOT_FOUND));
    }
}
