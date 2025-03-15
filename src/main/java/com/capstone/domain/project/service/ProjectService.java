package com.capstone.domain.project.service;

import com.capstone.domain.project.exception.InvalidMemberException;
import com.capstone.domain.project.exception.ProjectInvalidAccessException;
import com.capstone.domain.project.dto.request.ProjectAuthorityRequest;
import com.capstone.domain.project.dto.request.ProjectSaveRequest;
import com.capstone.domain.project.entity.Project;
import com.capstone.domain.project.exception.ProjectNotFoundException;
import com.capstone.domain.project.message.ProjectMessages;
import com.capstone.domain.project.repository.ProjectRepository;
import com.capstone.domain.user.service.UserService;
import com.capstone.global.jwt.JwtUtil;
import com.capstone.global.kafka.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final KafkaProducerService kafkaProducerService;
    private final JwtUtil jwtUtil;

    @Transactional
    public Project saveProject(ProjectSaveRequest projectSaveRequest){
        return projectRepository.save(projectSaveRequest.toProject(createDefaultAuthorities(Objects.requireNonNull(projectSaveRequest.invitedEmails()))));
    }


    @Transactional
    public void updateProject(ProjectSaveRequest projectSaveRequest){
        Project project = findProjectByProjectIdOrThrow(projectSaveRequest.projectId());
        project.updateProjectInfo(projectSaveRequest);
        projectRepository.save(project);
    }

    public Project getProjectContent(String projectId, String accessToken){
        return checkUserInProject(projectId, accessToken);
    }

    public void sendInvitation(ProjectAuthorityRequest projectAuthorityRequest){
        Project project = findProjectByProjectIdOrThrow(projectAuthorityRequest.projectId());
        userService.participateProcess(projectAuthorityRequest.getAuthorityKeysAsList(), projectAuthorityRequest.projectId());
        kafkaProducerService.sendProjectEvent("update-event", "INVITE", project.getProjectName(), projectAuthorityRequest.authorities());
        kafkaProducerService.sendMailEvent("mail-event", projectAuthorityRequest.getAuthorityKeysAsList());
    }


    public Map<String, String> createDefaultAuthorities(List<String> invitaionList){
        return invitaionList.stream()
                .collect(Collectors.toMap(email -> email, email-> "ROLE_USER"));
    }

    public void processRegister(ProjectSaveRequest projectSaveRequest){
        Project project = saveProject(projectSaveRequest);
        userService.participateProcess(Objects.requireNonNull(projectSaveRequest.invitedEmails()), project.getId());
        kafkaProducerService.sendProjectEvent("update-event", "REGISTER", project.getProjectName(), findProjectByProjectIdOrThrow(project.getId()).getAuthorities());
        kafkaProducerService.sendMailEvent("mail-event", projectSaveRequest.invitedEmails());
    }

    @Transactional
    public void processAuth(ProjectAuthorityRequest projectAuthorityRequest){
        Project project = findProjectByProjectIdOrThrow(projectAuthorityRequest.projectId());
        projectRepository.updateAuthority(projectAuthorityRequest);
        kafkaProducerService.sendProjectEvent("update-event", "AUTH", project.getProjectName(), project.getAuthorities());
        kafkaProducerService.sendMailEvent("mail-event", projectAuthorityRequest.getAuthorityKeysAsList());
    }

    public void processUpdate(ProjectSaveRequest projectSaveRequest){
        updateProject(projectSaveRequest);
        kafkaProducerService.sendProjectEvent("update-event", "UPDATE", projectSaveRequest.projectName(), findProjectByProjectIdOrThrow(projectSaveRequest.projectId()).getAuthorities());
        kafkaProducerService.sendMailEvent("mail-event", projectRepository.getAuthorityKeysByProjectId(projectSaveRequest.projectId()));
    }

    public void processInvite(ProjectAuthorityRequest projectAuthorityRequest) {
        Project project = findProjectByProjectIdOrThrow(projectAuthorityRequest.projectId());
        Map<String, String> existingAuthorities = project.getAuthorities();

        Map<String, String> newInvites = projectAuthorityRequest.authorities().entrySet().stream()
                .filter(entry -> !existingAuthorities.containsKey(entry.getKey())) // 기존 참여자가 아닌 사람만 남김
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (!newInvites.isEmpty()) {
            projectRepository.updateAuthority(new ProjectAuthorityRequest(projectAuthorityRequest.projectId(), newInvites));
            sendInvitation(new ProjectAuthorityRequest(projectAuthorityRequest.projectId(), newInvites));
        }
    }

    public Project findProjectByProjectIdOrThrow(String projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
    }

    public Project checkUserInProject(String projectId, String accessToken){
        Project project = findProjectByProjectIdOrThrow(projectId);
        if (!project.getAuthorities().containsKey(jwtUtil.getEmail(accessToken))){
            throw new ProjectInvalidAccessException();
        }
        return project;
    }
}
