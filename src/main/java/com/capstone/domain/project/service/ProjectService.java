package com.capstone.domain.project.service;

import com.capstone.domain.notification.service.NotificationService;
import com.capstone.domain.project.exception.ProjectInvalidAccessException;
import com.capstone.domain.user.repository.UserRepository;
import com.capstone.domain.project.dto.AuthorityRequest;
import com.capstone.domain.project.dto.ProjectDto;
import com.capstone.domain.project.entity.Project;
import com.capstone.domain.project.exception.ProjectNotFoundException;
import com.capstone.domain.project.message.ProjectMessages;
import com.capstone.domain.project.repository.ProjectRepository;
import com.capstone.domain.user.service.UserService;
import com.capstone.global.jwt.JwtUtil;
import com.capstone.global.kafka.service.KafkaProducerService;
import com.capstone.global.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final KafkaProducerService kafkaProducerService;
    private final JwtUtil jwtUtil;

    @Transactional
    public Project saveProject(ProjectDto projectDto){
        return projectRepository.save(Project.createProject(projectDto, createDefaultAuthorities(Objects.requireNonNull(projectDto.getInvitedEmails()))));
    }


    @Transactional
    public void updateProject(ProjectDto projectDto){
        Project project = findProjectByProjectIdOrThrow(projectDto.getProjectId());
        project.setProjectName(projectDto.getProjectName());
        project.setDescription(projectDto.getDescription());
    }




    @Transactional
    public void updateAuthority(AuthorityRequest authorityRequest){
        Project project = findProjectByProjectIdOrThrow(authorityRequest.getProjectId());
        project.updateAllAuthorities(authorityRequest.getAuthorities());
    }

    public Project getProjectContent(String projectId, String accessToken){
        return checkUserInProject(projectId, accessToken);
    }

    public void sendInvitation(AuthorityRequest authorityRequest){
        Project project = findProjectByProjectIdOrThrow(authorityRequest.getProjectId());
        userService.participateProcess(authorityRequest.getAuthorityKeysAsList(), authorityRequest.getProjectId());
        kafkaProducerService.sendProjectEvent("update-event", "INVITE", project.getProjectName(), authorityRequest.getAuthorities());
        kafkaProducerService.sendMailEvent("mail-event", authorityRequest.getAuthorityKeysAsList());
    }


    public Map<String, String> createDefaultAuthorities(List<String> invitaionList){
        return invitaionList.stream()
                .collect(Collectors.toMap(email -> email, email-> "ROLE_USER"));
    }

    public void processRegister(ProjectDto projectDto){
        Project project = saveProject(projectDto);
        userService.participateProcess(Objects.requireNonNull(projectDto.getInvitedEmails()), project.getId());
        kafkaProducerService.sendProjectEvent("update-event", "REGISTER", project.getProjectName(), findProjectByProjectIdOrThrow(project.getId()).getAuthorities());
        kafkaProducerService.sendMailEvent("mail-event", projectDto.getInvitedEmails());
    }

    public void processAuth(AuthorityRequest authorityRequest){
        Project project = findProjectByProjectIdOrThrow(authorityRequest.getProjectId());
        updateAuthority(authorityRequest);
        kafkaProducerService.sendProjectEvent("update-event", "AUTH", project.getProjectName(), project.getAuthorities());
        kafkaProducerService.sendMailEvent("mail-event", authorityRequest.getAuthorityKeysAsList());
    }

    public void processUpdate(ProjectDto projectDto){
        updateProject(projectDto);
        kafkaProducerService.sendProjectEvent("update-event", "UPDATE", projectDto.getProjectName(), findProjectByProjectIdOrThrow(projectDto.getProjectId()).getAuthorities());
        kafkaProducerService.sendMailEvent("mail-event", projectRepository.getAuthorityKeysByProjectId(projectDto.getProjectId()));
    }

    public void processInvite(AuthorityRequest authorityRequest){
        updateAuthority(authorityRequest);
        sendInvitation(authorityRequest);
    }

    public Project findProjectByProjectIdOrThrow(String projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ProjectMessages.PROJECT_NOT_FOUND));
    }

    public Project checkUserInProject(String projectId, String accessToken){
        Project project = findProjectByProjectIdOrThrow(projectId);
        if (!project.getAuthorities().containsKey(jwtUtil.getEmail(accessToken))){
            throw new ProjectInvalidAccessException(ProjectMessages.PROJECT_NOT_ACCESS);
        }
        return project;
    }
}
