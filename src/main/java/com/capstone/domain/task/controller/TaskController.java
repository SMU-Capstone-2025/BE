package com.capstone.domain.task.controller;

import com.capstone.domain.task.dto.TaskDto;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;
import com.capstone.domain.task.service.TaskService;
import com.capstone.global.elastic.entity.LogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TaskController {

    private final TaskService taskService;

    // TODO: ApiResponse 생성해서 응답이 일관되도록 보장.
    // TODO: 사용자의 토큰을 기반으로 작업이 포함된 프로젝트에 참여 중인지 확인.
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

    @PostMapping("/version/new")
    public ResponseEntity<String> postVersion(@RequestBody TaskDto taskDto){
        return ResponseEntity.ok(taskService.addVersion(taskDto));
    }

    @GetMapping("/version/list")
    public ResponseEntity<List<Version>> getVersionLists(@RequestParam String taskId){
        return ResponseEntity.ok(taskService.listVersions(taskId));
    }

    @GetMapping("/version/back")
    public ResponseEntity<Task> getVersionRollback(@RequestParam String taskId, @RequestParam String version){
        return ResponseEntity.ok(taskService.rollbackVersion(taskId, version));
    }

    @PutMapping("/version/modify")
    public ResponseEntity<String> versionModify(@RequestBody TaskDto taskDto){
        return ResponseEntity.ok(taskService.modifyVersion(taskDto));
    }

    @GetMapping("/log")
    public ResponseEntity<List<LogEntity>> getLogList(@RequestParam String taskId){
        return ResponseEntity.ok(taskService.findLogsByTaskId(taskId));
    }
}
