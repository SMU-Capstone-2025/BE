package com.capstone.domain.project.controller;

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
}
