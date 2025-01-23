package com.capstone.task.controller;

import com.capstone.task.dto.TaskDto;
import com.capstone.task.entity.Task;
import com.capstone.task.entity.Version;
import com.capstone.task.repository.TaskRepository;
import com.capstone.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // TODO: ApiResponse 생성해서 응답이 일관되도록 보장.
    @PostMapping("/post")
    public ResponseEntity<Task> postTask(@RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.saveTask(taskDto));
    }

    @PutMapping("/put")
    public ResponseEntity<String> putTask(@RequestBody TaskDto taskDto){
        return ResponseEntity.ok(taskService.updateTask(taskDto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTask(@RequestParam String id){
        return ResponseEntity.ok(taskService.dropTask(id));
    }

}
