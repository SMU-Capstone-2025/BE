package com.capstone.domain.project.service;

import com.capstone.domain.project.dto.response.ProjectListResponse;
import com.capstone.domain.project.exception.InvalidMemberException;
import com.capstone.domain.project.exception.ProjectInvalidAccessException;
import com.capstone.domain.project.dto.request.ProjectAuthorityRequest;
import com.capstone.domain.project.dto.request.ProjectSaveRequest;
import com.capstone.domain.project.entity.Project;
import com.capstone.domain.project.exception.ProjectNotFoundException;
import com.capstone.domain.project.message.ProjectMessages;
import com.capstone.domain.project.repository.ProjectRepository;
import com.capstone.domain.user.entity.User;
import com.capstone.domain.user.service.UserService;
import com.capstone.global.jwt.JwtUtil;
import com.capstone.global.kafka.service.KafkaProducerService;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;
import com.capstone.global.security.CustomUserDetails;
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

    @Transactional
    public Project saveProject(ProjectSaveRequest projectSaveRequest){
        return projectRepository.save(projectSaveRequest.toProject(createDefaultAuthorities(Objects.requireNonNull(projectSaveRequest.invitedEmails()))));
    }


    @Transactional
    public Project updateProject(ProjectSaveRequest projectSaveRequest){
        Project project = findProjectByProjectIdOrThrow(projectSaveRequest.projectId());
        project.updateProjectInfo(projectSaveRequest);
        projectRepository.save(project);
        return project;
    }

    public Project getProjectContent(String projectId, CustomUserDetails customUserDetails){
        return checkUserInProject(projectId, customUserDetails);
    }

    public void sendInvitation(ProjectAuthorityRequest projectAuthorityRequest){
        Project project = findProjectByProjectIdOrThrow(projectAuthorityRequest.projectId());
        userService.participateProcess(projectAuthorityRequest.getAuthorityKeysAsList(), projectAuthorityRequest.projectId());
        kafkaProducerService.sendProjectChangedEvent(
                "project.changed",
                "INVITE",
                projectAuthorityRequest.authorities(),
                projectAuthorityRequest.getAuthorityKeysAsList()
        );
    }


    public Map<String, String> createDefaultAuthorities(List<String> invitaionList){
        return invitaionList.stream()
                .collect(Collectors.toMap(email -> email, email-> "ROLE_USER"));
    }

    public Project processRegister(ProjectSaveRequest projectSaveRequest){
        Project project = saveProject(projectSaveRequest);
        userService.participateProcess(Objects.requireNonNull(projectSaveRequest.invitedEmails()), project.getId());
        kafkaProducerService.sendProjectChangedEvent(
                "project.changed",
                "CREATE",
                null,
                projectSaveRequest.invitedEmails()
        );
        return project;
    }

    @Transactional
    public Project processAuth(ProjectAuthorityRequest projectAuthorityRequest){
        Project project = findProjectByProjectIdOrThrow(projectAuthorityRequest.projectId());
        projectRepository.updateAuthority(projectAuthorityRequest);
        kafkaProducerService.sendProjectChangedEvent(
                "project.changed",
                "AUTH",
                project,
                projectAuthorityRequest.getAuthorityKeysAsList()
        );
        return project;
    }

    @Transactional
    public Project processUpdate(ProjectSaveRequest projectSaveRequest){
        kafkaProducerService.sendProjectChangedEvent(
                "project.changed",
                "UPDATE",
                null,
                projectSaveRequest.invitedEmails()
        );
        return updateProject(projectSaveRequest);
    }

    @Transactional
    public Project processInvite(ProjectAuthorityRequest projectAuthorityRequest) {
        Project project = findProjectByProjectIdOrThrow(projectAuthorityRequest.projectId());
        Map<String, String> existingAuthorities = project.getAuthorities();
        Map<String, String> newInvites = projectAuthorityRequest.authorities().entrySet().stream()
                .filter(entry -> !existingAuthorities.containsKey(entry.getKey())) // 기존 참여자가 아닌 사람만 남김
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (!newInvites.isEmpty()) {
            project.addAuthorities(newInvites);
            projectRepository.save(project);
            sendInvitation(new ProjectAuthorityRequest(projectAuthorityRequest.projectId(), newInvites));
        }

        kafkaProducerService.sendProjectChangedEvent(
                "project.changed",
                "INVITE",
                project,
                projectAuthorityRequest.getAuthorityKeysAsList()
        );
        return project;
    }

    public Project findProjectByProjectIdOrThrow(String projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new GlobalException(ErrorStatus.PROJECT_NOT_FOUND));
    }

    public Project checkUserInProject(String projectId, CustomUserDetails customUserDetails){
        Project project = findProjectByProjectIdOrThrow(projectId);
        if (!project.getAuthorities().containsKey(customUserDetails.getEmail())){
            throw new GlobalException(ErrorStatus.INVALID_MEMBER);
        }
        return project;
    }

    public List<ProjectListResponse> getProjectList(CustomUserDetails customUserDetails) {
        return customUserDetails.getProjects().stream()
                .map(ProjectListResponse::from)
                .toList();
    }
}
