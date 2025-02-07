package com.capstone.domain.project.controller;

import com.capstone.domain.project.dto.AuthorityRequest;
import com.capstone.domain.project.dto.ProjectDto;
import com.capstone.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("/register")
    public void registerProject(@RequestBody ProjectDto projectDto){
        projectService.processRegister(projectDto);
    }

    @PreAuthorize("ROLE_MANAGER")
    @PutMapping("/update")
    public void updateProject(@RequestBody ProjectDto projectDto){
        projectService.processUpdate(projectDto);
    }

    @PreAuthorize("ROLE_MANAGER")
    @PutMapping("/auth")
    public void updateAuthority(@RequestBody AuthorityRequest authorityRequest){
        projectService.processAuth(authorityRequest);
    }
}
