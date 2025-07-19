package com.capstone.domain.project.dto.response;

import com.capstone.domain.task.dto.response.TaskResponse;
import com.capstone.domain.task.entity.Task;
import lombok.Builder;

@Builder
public record ProjectCoworkerDto(
        String email,
        String role
) {
    public static ProjectCoworkerDto from(String email,String role) {
        return ProjectCoworkerDto.builder()
                .email(email)
                .role(role)
                .build();
    }
}
