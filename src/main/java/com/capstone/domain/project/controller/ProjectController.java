package com.capstone.domain.project.controller;

import com.capstone.docs.ProjectControllerDocs;
import com.capstone.domain.project.dto.request.ProjectAuthorityRequest;
import com.capstone.domain.project.dto.request.ProjectSaveRequest;
import com.capstone.domain.project.dto.request.ProjectUpdateRequest;
import com.capstone.domain.project.dto.response.ProjectResponse;
import com.capstone.domain.project.entity.Project;
import com.capstone.domain.project.service.ProjectService;

import com.capstone.domain.user.service.ProjectUserService;
import com.capstone.global.response.ApiResponse;
import com.capstone.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController implements ProjectControllerDocs {
    private final ProjectService projectService;
    private final ProjectUserService projectUserService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Project>> registerProject(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody ProjectSaveRequest projectSaveRequest){

        return ResponseEntity.ok(ApiResponse.onSuccess(projectService.processRegister(customUserDetails, projectSaveRequest)));
    }


    @PutMapping("/update")
    @PreAuthorize("@projectAuthorityEvaluator.hasPermission(#projectUpdateRequest.projectId, {'ROLE_MANAGER','ROLE_MEMBER'}, authentication)")
    public ResponseEntity<ApiResponse<Project>> updateProject(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody ProjectUpdateRequest projectUpdateRequest){
        return ResponseEntity.ok(ApiResponse.onSuccess(projectService.processUpdate(projectUpdateRequest, customUserDetails)));
    }

    @PutMapping("/auth")
    @PreAuthorize("@projectAuthorityEvaluator.hasPermission(#projectAuthorityRequest.projectId, {'ROLE_MANAGER','ROLE_MEMBER'}, authentication)")
    public ResponseEntity<ApiResponse<Project>> updateAuthority(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ProjectAuthorityRequest projectAuthorityRequest){
        projectAuthorityRequest.validateRoles();
        return ResponseEntity.ok(ApiResponse.onSuccess(projectUserService.processAuth(customUserDetails, projectAuthorityRequest)));
    }

    @PutMapping("/invite")
    @PreAuthorize("@projectAuthorityEvaluator.hasPermission(#projectAuthorityRequest.projectId, {'ROLE_MANAGER','ROLE_MEMBER'}, authentication)")
    public ResponseEntity<ApiResponse<Project>> inviteProject(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ProjectAuthorityRequest projectAuthorityRequest){
        projectAuthorityRequest.validateRoles();
        return ResponseEntity.ok(ApiResponse.onSuccess(projectUserService.processInvite(customUserDetails, projectAuthorityRequest)));
    }

    @GetMapping("/load")
    @PreAuthorize("@projectAuthorityEvaluator.hasPermission(#projectId, {'ROLE_MANAGER','ROLE_MEMBER'}, authentication)")
    public ResponseEntity<ApiResponse<ProjectResponse>> loadProject(@RequestParam String projectId){
        return ResponseEntity.ok(ApiResponse.onSuccess(projectService.getProjectContent(projectId)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> loadProjectList(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(ApiResponse.onSuccess(projectService.getProjectList(userDetails)));
    }

}
