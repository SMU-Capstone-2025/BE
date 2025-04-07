package com.capstone.domain.task.service;

import com.capstone.domain.task.dto.request.TaskRequest;
import com.capstone.domain.task.dto.response.TaskResponse;
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
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;
import com.capstone.global.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final LogRepository logRepository;
    private final KafkaProducerService kafkaProducerService;
    private final TaskUtil taskUtil;

    @Transactional
    public Task saveTask(TaskRequest taskDto){
        return taskRepository.save(taskDto.toTask());
    }

    public Version loadVersionContent(String taskId) {
        Task task = findTaskByIdOrThrow(taskId);
        String currentVersion = task.getCurrentVersion();
        return taskRepository.findByTaskIdAndVersion(taskId, currentVersion);
    }

    @Transactional
    public Version saveVersion(TaskRequest taskDto, String fileId, CustomUserDetails customUserDetails){
        Version version = taskUtil.createOrGetVersion(taskDto, fileId);
        Task task = findTaskByIdOrThrow(taskDto.id());
        task.addNewVersion(version);
        taskRepository.save(task);

        kafkaProducerService.sendTaskEvent("task.changed", "ADD", taskDto, customUserDetails.getEmail());
        return version;
    }

    @Transactional
    public Task dropTask(String id, CustomUserDetails userDetails){
        log.info("userDetails: {}", userDetails.getEmail());
        Task task = findTaskByIdOrThrow(id);
        taskRepository.delete(task);
        kafkaProducerService.sendTaskEvent("task.changed", "DELETE", task, userDetails.getEmail());
        return task;
    }

    @Transactional
    public Task updateStatus(String id, String status, CustomUserDetails userDetails){
        Task task = findTaskByIdOrThrow(id);
        task.updateStatus(status);
        taskRepository.save(task);
        kafkaProducerService.sendTaskEvent("task.changed", "UPDATE", task, userDetails.getEmail());

        return task;
    }

    public List<Version> listVersions(String taskId){
        Task task = findTaskByIdOrThrow(taskId);
        return task.getVersionHistory();
    }

    @Transactional
    public TaskResponse rollbackVersion(String taskId, String version){
        Task task = findTaskByIdOrThrow(taskId);
        task.updateCurrentVersion(version);
        taskRepository.save(task);
        return TaskResponse.from(task);
    }


    public List<LogEntity> findLogsByTaskId(String taskId){
        return logRepository.findAllByTaskId(taskId);
    }

    public Task findTaskByIdOrThrow(String id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorStatus.TASK_NOT_FOUND));
    }
}
