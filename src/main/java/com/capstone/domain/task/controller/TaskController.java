package com.capstone.domain.task.controller;

import com.capstone.domain.task.dto.TaskDto;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;
import com.capstone.domain.task.service.TaskService;
import com.capstone.domain.log.entity.LogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('MEMBER', 'MANAGER')")
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/post")
    public ResponseEntity<String> postTask(@RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.saveTask(taskDto));
    }

    @GetMapping("/get")
    public ResponseEntity<Version> getTask(@RequestParam String taskId){
        return ResponseEntity.ok(taskService.loadVersionContent(taskId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTask(@RequestParam String id){
        return ResponseEntity.ok(taskService.dropTask(id));
    }

    @PutMapping("/status")
    public ResponseEntity<String> putStatus(@RequestParam String id, @RequestParam String status){
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }

    @PostMapping("/version/save")
    public ResponseEntity<String> postVersion(@RequestHeader("Authorization") String token,
                                              @RequestBody TaskDto taskDto,
                                              @RequestParam(value = "fileId", required = false) String fileId){
        return ResponseEntity.ok(taskService.saveVersion(taskDto, fileId, token));
    }

    @GetMapping("/version/list")
    public ResponseEntity<List<Version>> getVersionLists(@RequestParam String taskId){
        return ResponseEntity.ok(taskService.listVersions(taskId));
    }

    @GetMapping("/version/back")
    public ResponseEntity<Task> getVersionRollback(@RequestParam String taskId, @RequestParam String version){
        return ResponseEntity.ok(taskService.rollbackVersion(taskId, version));
    }

    @GetMapping("/log")
    public ResponseEntity<List<LogEntity>> getLogList(@RequestParam String taskId){
        return ResponseEntity.ok(taskService.findLogsByTaskId(taskId));
    }
}
