package com.capstone.task.service;

import com.capstone.task.dto.TaskDto;
import com.capstone.task.entity.Task;
import com.capstone.task.entity.Version;
import com.capstone.task.repository.TaskRepository;
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

    public Version createVersion(TaskDto taskDto){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        Version version =  Version.builder()
                .version(taskDto.getVersion())
                .modifiedDateTime(formattedDateTime)
                .modifiedBy(taskDto.getModifiedBy())
                .summary(taskDto.getSummary())
                .content(taskDto.getContent())
                .build();

        return version;
    }
    public Task createTask(TaskDto taskDto){
        Task task = Task.builder()
                .title(taskDto.getTitle())
                .currentVersion(taskDto.getVersion())
                .versionHistory(new ArrayList<>())
                .build();
        return task;
    }
    public String updateTask(TaskDto taskDto){
        Version version = createVersion(taskDto);
        Optional<Task> optionalTask = fetchTaskById(taskDto.getId());
        if(optionalTask.isPresent()){
            Task task = optionalTask.get();
            if(task.getCurrentVersion() == null){
                task.setVersionHistory(new ArrayList<>());
            }
            task.getVersionHistory().add(version);
            putTask(task);
        }
        return "업데이트가 성공적으로 완료되었습니다.";
    }
    public Optional<Task> fetchTaskById(String id){
        return taskRepository.findById(id);
    }

    public Task saveTask(TaskDto taskDto){
        return taskRepository.save(createTask(taskDto));
    }

    public void putTask(Task task){
        taskRepository.save(task);
    }
}
