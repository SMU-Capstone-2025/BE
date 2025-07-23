package com.capstone.global.kafka.dto;

import com.capstone.domain.project.entity.Project;
import com.capstone.global.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInvitePayload extends CommonChangePayload {
    private String invitor;
    private String invitee;
    private String projectId;
    private String projectName;

    public static ProjectInvitePayload from(Project project, CustomUserDetails customUserDetails,
                                            String invitee) {
        return ProjectInvitePayload.builder()
                .invitor(customUserDetails.getUsername())
                .invitee(invitee)
                .projectName(project.getProjectName())
                .projectId(project.getId())
                .build();
    }
}
