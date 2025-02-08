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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        return projectRepository.save(Project.createProject(projectDto, createDefaultAuthorities(projectDto.getInvitedEmails())));
    }


    @Transactional
    public void updateProject(ProjectDto projectDto){
        Project project = findProjectByProjectIdOrThrow(projectDto.getProjectId());
        project.setProjectName(projectDto.getProjectName());
        project.setDescription(projectDto.getDescription());
        projectRepository.save(project);
    }


    @Transactional
    public void updateAuthority(AuthorityRequest authorityRequest){
        Project project = findProjectByProjectIdOrThrow(authorityRequest.getProjectId());
        Map<String, String> oldAuthorities = project.getAuthorities();

        authorityRequest.getAuthorities().forEach(
                (email, newRole) -> {
                    if (oldAuthorities.containsKey(email)){
                        oldAuthorities.replace(email, newRole);
                    }
                }
        );

        projectRepository.save(project);
    }

    public Project getProjectContent(String projectId, String accessToken){
        return checkUserInProject(projectId, accessToken);
    }
    public void sendInvitation(AuthorityRequest authorityRequest){
        userService.participateProcess(authorityRequest.getAuthorityKeysAsList(), authorityRequest.getProjectId());
        kafkaProducerService.sendProjectEvent("update-event", "REGISTER", authorityRequest.getProjectId(), authorityRequest.getAuthorityKeysAsList());
        kafkaProducerService.sendMailEvent("mail-event", authorityRequest.getAuthorityKeysAsList());
    }


    public Map<String, String> createDefaultAuthorities(List<String> invitaionList){
        return invitaionList.stream()
                .collect(Collectors.toMap(email -> email, email-> "ROLE_USER"));
    }

    public void processRegister(ProjectDto projectDto){
        String projectId = saveProject(projectDto).getId();
        userService.participateProcess(projectDto.getInvitedEmails(), projectId);
        kafkaProducerService.sendProjectEvent("update-event", "REGISTER", projectId, projectDto.getInvitedEmails());
        kafkaProducerService.sendMailEvent("mail-event", projectDto.getInvitedEmails());
    }

    public void processAuth(AuthorityRequest authorityRequest){
        updateAuthority(authorityRequest);
        kafkaProducerService.sendProjectEvent("update-event", "AUTH", authorityRequest.getProjectId(), authorityRequest.getAuthorityKeysAsList());
        kafkaProducerService.sendMailEvent("mail-event", authorityRequest.getAuthorityKeysAsList());
    }

    public void processUpdate(ProjectDto projectDto){
        updateProject(projectDto);
        kafkaProducerService.sendProjectEvent("update-event", "UPDATE", projectDto.getProjectId(), projectDto.getInvitedEmails());
        kafkaProducerService.sendMailEvent("mail-event", projectDto.getInvitedEmails());
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
