package com.capstone.domain.project.service;

import com.capstone.domain.project.dto.ProjectDto;
import com.capstone.domain.project.entity.Project;
import com.capstone.domain.project.repository.ProjectRepository;
import com.capstone.global.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MailService mailService;

    public void createProject(ProjectDto projectDto) {
        projectRepository.save(
                Project.builder()
                        .projectName(projectDto.getProjectName())
                        .description(projectDto.getDescription())
                        .authorities(createDefaultAuthorities(projectDto.getInvitedEmails()))
                        .projectIds(new ArrayList<>())
                        .documentIds(new ArrayList<>())
                        .build()
        );
    }

    public Map<String, String> createDefaultAuthorities(List<String> invitaionList){
        return invitaionList.stream()
                .collect(Collectors.toMap(email -> email, email-> "ROLE_USER"));
    }

    public void sendInvitation(List<String> invitaionList){
        mailService.sendMultipleMessages(invitaionList);
    }

    public void processRegister(ProjectDto projectDto){
        createProject(projectDto);
        sendInvitation(projectDto.getInvitedEmails());
    }
}
