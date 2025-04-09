package com.capstone.domain.task.controller;

import com.capstone.docs.TaskControllerDocs;
import com.capstone.domain.task.dto.request.TaskRequest;
import com.capstone.domain.task.dto.response.TaskResponse;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;
import com.capstone.domain.task.service.TaskService;
import com.capstone.domain.log.entity.LogEntity;
import com.capstone.global.response.ApiResponse;
import com.capstone.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MEMBER', 'MANAGER')")
public class TaskController implements TaskControllerDocs {

    private final TaskService taskService;

    @PostMapping("/post")
    public ResponseEntity<ApiResponse<Task>> postTask(@Valid @RequestBody TaskRequest taskDto) {
        return ResponseEntity.ok(ApiResponse.onSuccess(taskService.saveTask(taskDto)));
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<Version>> getTask(@RequestParam String taskId){
        return ResponseEntity.ok(ApiResponse.onSuccess(taskService.loadVersionContent(taskId)));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Task>> deleteTask(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String id){
        return ResponseEntity.ok(ApiResponse.onSuccess(taskService.dropTask(id, userDetails)));
    }

    @PutMapping("/status")
    public ResponseEntity<ApiResponse<Task>> putStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String id,
            @RequestParam String status){
        return ResponseEntity.ok(ApiResponse.onSuccess(taskService.updateStatus(id, status, userDetails)));
    }

    @PostMapping("/version/save")
    public ResponseEntity<ApiResponse<Version>> postVersion(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @Valid @RequestBody TaskRequest taskDto,
                                                            @RequestParam(value = "fileId", required = false) String fileId){
        return ResponseEntity.ok(ApiResponse.onSuccess(taskService.saveVersion(taskDto, fileId, userDetails)));
    }

    @GetMapping("/version/list")
    public ResponseEntity<ApiResponse<List<Version>>> getVersionLists(@RequestParam String taskId){
        return ResponseEntity.ok(ApiResponse.onSuccess(taskService.listVersions(taskId)));
    }

    @GetMapping("/version/back")
    public ResponseEntity<ApiResponse<TaskResponse>> getVersionRollback(@RequestParam String taskId, @RequestParam String version){
        return ResponseEntity.ok(ApiResponse.onSuccess(taskService.rollbackVersion(taskId, version)));
    }

    @GetMapping("/log")
    public ResponseEntity<ApiResponse<List<LogEntity>>> getLogList(@RequestParam String taskId){
        return ResponseEntity.ok(ApiResponse.onSuccess(taskService.findLogsByTaskId(taskId)));
    }


    @GetMapping("/list/deadline")
    public ResponseEntity<ApiResponse<List<Task>>> getDeadlineList(@AuthenticationPrincipal CustomUserDetails userDetails)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(taskService.listByDeadLine(userDetails)));
    }
    @GetMapping("/list/get")
    public ResponseEntity<ApiResponse<List<Task>>> getList(@AuthenticationPrincipal CustomUserDetails userDetails)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(taskService.listTask(userDetails)));
    }

}
