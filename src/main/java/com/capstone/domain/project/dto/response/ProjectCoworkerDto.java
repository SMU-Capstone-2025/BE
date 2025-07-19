package com.capstone.domain.project.dto.response;

import com.capstone.domain.task.dto.response.TaskResponse;
import com.capstone.domain.task.entity.Task;
import lombok.Builder;

@Builder
public record ProjectCoworkerDto(
        String email,
        String role,
        String name
) {
    public static ProjectCoworkerDto from(String email,String role,String name) {
        return ProjectCoworkerDto.builder()
                .email(email)
                .role(role)
                .name(name)
                .build();
    }
}
