package com.capstone.domain.project.service;

import com.capstone.domain.project.dto.request.ProjectUpdateRequest;
import com.capstone.domain.project.dto.response.ProjectResponse;
import com.capstone.domain.project.dto.request.ProjectAuthorityRequest;
import com.capstone.domain.project.dto.request.ProjectSaveRequest;
import com.capstone.domain.project.entity.Project;
import com.capstone.domain.project.repository.ProjectRepository;

import com.capstone.domain.user.entity.ProjectUser;
import com.capstone.domain.user.repository.ProjectUserRepository;
import com.capstone.domain.user.service.UserService;

import com.capstone.global.kafka.dto.ProjectChangePayload;
import com.capstone.global.kafka.dto.detail.ProjectChangeDetail;
import com.capstone.global.kafka.service.KafkaProducerService;
import com.capstone.global.kafka.topic.KafkaEventTopic;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;
import com.capstone.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final KafkaProducerService kafkaProducerService;
    private final ProjectUserRepository projectUserRepository;


    @Transactional
    public Project saveProject(CustomUserDetails customUserDetails, ProjectSaveRequest projectSaveRequest){
        System.out.println(projectSaveRequest.projectName());
        Project project = projectRepository.save(projectSaveRequest.toProject());
        if (projectSaveRequest.invitedEmails() != null && !projectSaveRequest.invitedEmails().isEmpty()) {
            List<ProjectUser> projectUsers = new ArrayList<>(projectSaveRequest.invitedEmails().stream()
                    .map(email -> ProjectUser.builder()
                            .projectId(project.getId())
                            .userId(email) // userId 또는 email로 처리, 시스템 구조에 따라
                            .role("ROLE_MEMBER") // 기본 권한 설정
                            .status("INVITED")
                            .joinedAt(LocalDate.now().toString())
                            .build()
                    ).toList());


            ProjectUser inviteUser = ProjectUser.builder()
                    .projectId(project.getId())
                    .userId(customUserDetails.getEmail())
                    .role("ROLE_MEMBER")
                    .status("ACCEPTED")
                    .joinedAt(LocalDate.now().toString())
                    .build();

            projectUsers.add(inviteUser);

            projectUserRepository.saveAll(projectUsers);
        }
        return project;
    }




    public ProjectResponse getProjectContent(String projectId){
        return ProjectResponse.from(findProjectByProjectIdOrThrow(projectId), projectUserRepository.findUserIdByProjectId(projectId));
    }

    public Project processRegister(CustomUserDetails customUserDetails, ProjectSaveRequest projectSaveRequest){
        Project project = saveProject(customUserDetails, projectSaveRequest);
        userService.participateProcess(Objects.requireNonNull(projectSaveRequest.invitedEmails()), project.getId());
        kafkaProducerService.sendEvent(KafkaEventTopic.PROJECT_CREATED, ProjectChangePayload.from(project, null, null, customUserDetails.getEmail(), projectSaveRequest.invitedEmails()));
        return project;
    }



    @Transactional
    public Project processUpdate(ProjectUpdateRequest projectUpdateRequest, CustomUserDetails customUserDetails){
        Project project = findProjectByProjectIdOrThrow(projectUpdateRequest.projectId());
        ProjectChangeDetail beforeUpdate = ProjectChangeDetail.from(project);

        project.updateProjectInfo(projectUpdateRequest.projectName(), projectUpdateRequest.description());
        ProjectChangeDetail afterUpdate = ProjectChangeDetail.from(project);

        List<String> coworkers = projectUserRepository.findUserIdByProjectId(project.getId());
        kafkaProducerService.sendEvent(KafkaEventTopic.PROJECT_UPDATED, ProjectChangePayload.from(project, beforeUpdate, afterUpdate, customUserDetails.getEmail(), coworkers));
        projectRepository.save(project);
        return project;
    }

    public Project findProjectByProjectIdOrThrow(String projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new GlobalException(ErrorStatus.PROJECT_NOT_FOUND));
    }


    public List<ProjectResponse> getProjectList(CustomUserDetails customUserDetails) {
        List<String> projectIds = parseProjectIds(projectUserRepository.findByUserId(customUserDetails.getEmail()));
        List<Project> projects = projectRepository.findAllById(projectIds);

        return projects.stream()
                .map(project -> {
                    List<String> coworkers = projectUserRepository.findUserIdByProjectId(project.getId());
                    return ProjectResponse.from(project, coworkers);
                })
                .toList();
    }

    public List<String> parseProjectIds(List<ProjectUser> projectUsers){
        return projectUsers.stream()
                .map(ProjectUser::getProjectId)
                .toList();
    }

}
