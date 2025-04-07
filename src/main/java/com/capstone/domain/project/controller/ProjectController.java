package com.capstone.domain.project.controller;

import com.capstone.docs.ProjectControllerDocs;
import com.capstone.domain.project.dto.request.ProjectAuthorityRequest;
import com.capstone.domain.project.dto.request.ProjectSaveRequest;
import com.capstone.domain.project.entity.Project;
import com.capstone.domain.project.service.ProjectService;
import com.capstone.global.response.ApiResponse;
import com.capstone.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController implements ProjectControllerDocs {
    private final ProjectService projectService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Project>> registerProject(@Valid @RequestBody ProjectSaveRequest projectSaveRequest){
        return ResponseEntity.ok(ApiResponse.onSuccess(projectService.processRegister(projectSaveRequest)));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Project>> updateProject(@Valid @RequestBody ProjectSaveRequest projectSaveRequest){
        return ResponseEntity.ok(ApiResponse.onSuccess(projectService.processUpdate(projectSaveRequest)));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/auth")
    public ResponseEntity<ApiResponse<Project>> updateAuthority(@RequestBody ProjectAuthorityRequest projectAuthorityRequest){
        projectAuthorityRequest.validateRoles();
        return ResponseEntity.ok(ApiResponse.onSuccess(projectService.processAuth(projectAuthorityRequest)));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/invite")
    public ResponseEntity<ApiResponse<Project>> inviteProject(@RequestBody ProjectAuthorityRequest projectAuthorityRequest){
        projectAuthorityRequest.validateRoles();
        return ResponseEntity.ok(ApiResponse.onSuccess(projectService.processInvite(projectAuthorityRequest)));
    }

    @GetMapping("/load")
    public ResponseEntity<ApiResponse<Project>> loadProject(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam String projectId){
        return ResponseEntity.ok(ApiResponse.onSuccess(projectService.getProjectContent(projectId, userDetails)));
    }

}
