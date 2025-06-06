package com.capstone.domain.user.service;

import com.capstone.domain.project.dto.request.ProjectAuthorityRequest;
import com.capstone.domain.project.entity.Project;
import com.capstone.domain.project.repository.ProjectRepository;
import com.capstone.domain.project.service.ProjectService;
import com.capstone.domain.user.entity.ProjectUser;
import com.capstone.domain.user.repository.ProjectUserRepository;
import com.capstone.global.kafka.service.KafkaProducerService;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;
import com.capstone.global.security.CustomUserDetails;
import com.nimbusds.oauth2.sdk.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectUserService {
    private final ProjectUserRepository projectUserRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public boolean hasRole(String userId, String projectId, String requiredRole) {
        return projectUserRepository.findByProjectIdAndUserId(projectId, userId)
                .map(pm -> pm.getRole().equalsIgnoreCase(requiredRole))
                .orElse(false);
    }

    @Transactional
    public Project processInvite(ProjectAuthorityRequest projectAuthorityRequest) {
        Project project = projectRepository.findById(projectAuthorityRequest.projectId())
                .orElseThrow(()-> new GlobalException(ErrorStatus.PROJECT_NOT_FOUND));
        projectAuthorityRequest.from().forEach(
                (projectUserRepository::save)
        );
        userService.participateProcess(projectAuthorityRequest.getAuthorityKeysAsList(), projectAuthorityRequest.projectId());
        kafkaProducerService.sendProjectChangedEvent(
                "project.changed",
                "INVITE",
                project,
                projectAuthorityRequest.getAuthorityKeysAsList()
        );
        return project;
    }




    public void updateProjectUserAuthorities(ProjectAuthorityRequest request) {
        String projectId = request.projectId();
        Map<String, String> newAuthorities = request.authorities();

        List<ProjectUser> projectUsers = projectUserRepository.findByProjectId(projectId);

        projectUsers.forEach(projectUser -> {
            String newRole = newAuthorities.get(projectUser.getUserId());
            if (newRole != null) {
                projectUser.setRole(newRole); // 권한 업데이트
            }
        });

        projectUserRepository.saveAll(projectUsers);
    }
    @Transactional
    public Project processAuth(ProjectAuthorityRequest projectAuthorityRequest){
        Project project = findProjectByProjectIdOrThrow(projectAuthorityRequest.projectId());
        updateProjectUserAuthorities(projectAuthorityRequest);
        kafkaProducerService.sendProjectChangedEvent(
                "project.changed",
                "AUTH",
                project,
                projectAuthorityRequest.getAuthorityKeysAsList()
        );
        return project;
    }

    public Project findProjectByProjectIdOrThrow(String projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new GlobalException(ErrorStatus.PROJECT_NOT_FOUND));
    }
}