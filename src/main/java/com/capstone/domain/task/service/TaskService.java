package com.capstone.task.service;

import com.capstone.task.dto.TaskDto;
import com.capstone.task.entity.Task;
import com.capstone.task.entity.Version;
import com.capstone.task.exception.TaskNotFoundException;
import com.capstone.task.message.ResponseMessages;
import com.capstone.task.repository.TaskRepository;
import com.capstone.task.util.TaskUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public String saveTask(TaskDto taskDto){
        taskRepository.save(TaskUtil.toEntity(taskDto));
        return ResponseMessages.TASK_CREATED;
    }
    public String updateTask(TaskDto taskDto){
        Version version = TaskUtil.toVersion(taskDto);
        Task task = findTaskByIdOrThrow(taskDto.getId());

    }
    public String dropTask(String id){
        Task task = findTaskByIdOrThrow(id);
        taskRepository.delete(task);
        return ResponseMessages.TASK_DROPPED;
    }

    public String updateStatus(String id, String status){
        Task task = findTaskByIdOrThrow(id);
        task.setStatus(status);
        taskRepository.save(task);
        return ResponseMessages.STATUS_UPDATED;
    }

    public Task findTaskByIdOrThrow(String id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("존재하지 않는 작업 ID: " + id));
    }
}
