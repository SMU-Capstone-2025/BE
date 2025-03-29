package com.capstone.domain.project.controller;

import com.capstone.domain.project.dto.request.ProjectAuthorityRequest;
import com.capstone.domain.project.dto.request.ProjectSaveRequest;
import com.capstone.domain.project.entity.Project;
import com.capstone.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("/register")
    public void registerProject(@RequestBody ProjectSaveRequest projectSaveRequest){
        projectService.processRegister(projectSaveRequest);
    }

    @PreAuthorize("ROLE_MANAGER")
    @PutMapping("/update")
    public void updateProject(@RequestBody ProjectSaveRequest projectSaveRequest){
        projectService.processUpdate(projectSaveRequest);
    }

    @PreAuthorize("ROLE_MANAGER")
    @PutMapping("/auth")
    public void updateAuthority(@RequestBody ProjectAuthorityRequest projectAuthorityRequest){
        projectService.processAuth(projectAuthorityRequest);
    }

    @PreAuthorize("ROLE_MANAGER")
    @PutMapping("/invite")
    public void inviteProject(@RequestBody ProjectAuthorityRequest projectAuthorityRequest){
        projectService.processInvite(projectAuthorityRequest);
    }

    @GetMapping("/load")
    public ResponseEntity<Project> loadProject(@RequestHeader("Authorization") String accessToken, @RequestParam String projectId){
        return ResponseEntity.ok().body(projectService.getProjectContent(projectId, accessToken));
    }

}
