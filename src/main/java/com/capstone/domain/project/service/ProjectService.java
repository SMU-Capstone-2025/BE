package com.capstone.domain.project.service;

import com.capstone.domain.notification.service.NotificationService;
import com.capstone.domain.user.repository.UserRepository;
import com.capstone.domain.project.dto.AuthorityRequest;
import com.capstone.domain.project.dto.ProjectDto;
import com.capstone.domain.project.entity.Project;
import com.capstone.domain.project.exception.ProjectNotFoundException;
import com.capstone.domain.project.message.ProjectMessages;
import com.capstone.domain.project.repository.ProjectRepository;
import com.capstone.domain.user.service.UserService;
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
    private final MailService mailService;
    private final NotificationService notificationService;

    public Project createProject(ProjectDto projectDto) {
        return Project.builder()
                .projectName(projectDto.getProjectName())
                .description(projectDto.getDescription())
                .authorities(createDefaultAuthorities(projectDto.getInvitedEmails()))
                .projectIds(new ArrayList<>())
                .documentIds(new ArrayList<>())
                .build();
    }

    @Transactional
    public void updateProject(ProjectDto projectDto){
        Project project = findProjectByProjectIdOrThrow(projectDto.getProjectName());
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

    public Map<String, String> createDefaultAuthorities(List<String> invitaionList){
        return invitaionList.stream()
                .collect(Collectors.toMap(email -> email, email-> "ROLE_USER"));
    }

    public void sendMails(List<String> invitaionList){
        mailService.sendMultipleMessages(invitaionList);
    }

    public void processRegister(ProjectDto projectDto){
        String projectId = projectRepository.save(createProject(projectDto)).getId();
        userService.participateProcess(projectDto.getInvitedEmails(), projectId);
        kafkaProducerService.sendProjectEvent("update-event", "REGISTER", projectDto.getProjectId(), projectDto.getInvitedEmails());
        kafkaProducerService.sendMailEvent("mail-event", projectDto.getInvitedEmails());
    }

    public void processAuth(AuthorityRequest authorityRequest){
        updateAuthority(authorityRequest);
        sendMails(authorityRequest.getAuthorityKeysAsList());
        kafkaProducerService.sendProjectEvent("update-event", "AUTH", authorityRequest.getProjectId(), authorityRequest.getAuthorityKeysAsList());
        kafkaProducerService.sendMailEvent("mail-event", authorityRequest.getAuthorityKeysAsList());
    }

    public void processUpdate(ProjectDto projectDto){
        updateProject(projectDto);
        sendMails(projectRepository.getAuthorityKeysByProjectId(projectDto.getProjectId()));
        kafkaProducerService.sendProjectEvent("update-event", "UPDATE", projectDto.getProjectId(), projectDto.getInvitedEmails());
        kafkaProducerService.sendMailEvent("mail-event", projectDto.getInvitedEmails());
    }

    public Project findProjectByProjectIdOrThrow(String projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ProjectMessages.PROJECT_NOT_FOUND));
    }

}
