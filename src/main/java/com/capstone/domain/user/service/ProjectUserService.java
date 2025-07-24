package com.capstone.domain.user.service;

import com.capstone.domain.project.dto.request.ProjectAuthorityRequest;
import com.capstone.domain.project.dto.request.ProjectInviteRequest;
import com.capstone.domain.project.entity.Project;
import com.capstone.domain.project.repository.ProjectRepository;
import com.capstone.domain.user.entity.PendingUser;
import com.capstone.domain.user.entity.ProjectUser;
import com.capstone.domain.user.repository.PendingUserRepository;
import com.capstone.domain.user.repository.ProjectUserRepository;
import com.capstone.global.kafka.dto.ProjectAuthPayload;
import com.capstone.global.kafka.dto.ProjectInvitePayload;
import com.capstone.global.kafka.service.KafkaProducerService;
import com.capstone.global.kafka.topic.KafkaEventTopic;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;
import com.capstone.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectUserService {
    private final ProjectUserRepository projectUserRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ProjectRepository projectRepository;
    private final PendingUserRepository pendingUserRepository;

    @Transactional
    public void processInvite(CustomUserDetails customUserDetails, String projectId, ProjectInviteRequest projectInviteRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new GlobalException(ErrorStatus.PROJECT_NOT_FOUND));

        kafkaProducerService.sendEvent(KafkaEventTopic.PROJECT_INVITED, ProjectInvitePayload.from(project, customUserDetails, projectInviteRequest.email()));
    }

    @Transactional
    public void insertProjectUser(String credentialCode){
        PendingUser pendingUser = pendingUserRepository.findByCredentialCode(credentialCode);
        ProjectUser projectUser = ProjectUser.builder()
                .projectId(pendingUser.getProjectId())
                .userId(pendingUser.getUserId())
                .role("ROLE_MEMBER")
                .joinedAt(LocalTime.now().toString())
                .build();

        projectUserRepository.save(projectUser);
        pendingUserRepository.delete(pendingUser);

    }


    public void updateProjectUserAuthorities(String projectId, ProjectAuthorityRequest request) {

        ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(projectId, request.getUserEmail()).orElseThrow();
        System.out.println(projectUser.getRole());
        String newRole = request.getRole();

        if (newRole != null) {
            projectUser.updateRole(newRole);
        }
        System.out.println(projectUser.getRole());
        projectUserRepository.save(projectUser);
    }

    @Transactional
    public Project processAuth(CustomUserDetails customUserDetails, String projectId,
            ProjectAuthorityRequest projectAuthority){

        Project project = findProjectByProjectIdOrThrow(projectId);
        updateProjectUserAuthorities(projectId, projectAuthority);
        kafkaProducerService.sendEvent(KafkaEventTopic.PROJECT_AUTHENTICATED, ProjectAuthPayload.from(projectAuthority.getUserEmail(), project, projectAuthority.getRole()));
        return project;
    }

    public Project findProjectByProjectIdOrThrow(String projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new GlobalException(ErrorStatus.PROJECT_NOT_FOUND));
    }

    @Transactional
    public void deleteProjectUser(CustomUserDetails customUserDetails, String projectId,String email)
    {
        String managerEmail = customUserDetails.getEmail();
        ProjectUser manager = projectUserRepository.findByProjectIdAndUserId(projectId, managerEmail)
                .orElseThrow(() -> new GlobalException(ErrorStatus.PROJECT_NOT_FOUND));
        if(manager.getRole().equals("ROLE_MANAGER"))
        {
            ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(projectId, email)
                    .orElseThrow(() -> new GlobalException(ErrorStatus.PROJECT_NOT_FOUND));

            projectUserRepository.delete(projectUser);
        }
        else {
            throw new GlobalException(ErrorStatus.PROJECT_NOT_ACCESS);
        }



    }

}