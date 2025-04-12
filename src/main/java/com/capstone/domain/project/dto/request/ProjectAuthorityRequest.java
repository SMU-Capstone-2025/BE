package com.capstone.domain.project.dto.request;

import com.capstone.domain.user.entity.ProjectUser;
import com.capstone.domain.user.entity.Role;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public record ProjectAuthorityRequest(
        String projectId,
        Map<String, String> authorities
)
{
    public List<String> getAuthorityKeysAsList() {
        return authorities == null ? List.of() : new ArrayList<>(authorities.keySet());
    }

    public List<ProjectUser> from() {
        return authorities.entrySet().stream()
                .map(entry -> ProjectUser.builder()
                        .projectId(projectId)
                        .userId(entry.getKey())
                        .role(entry.getValue())
                        .status("ACCEPTED")
                        .joinedAt(LocalDate.now().toString())
                        .build())
                .toList();
    }

    public void validateRoles() {
        if (authorities == null) return;

        for (Map.Entry<String, String> entry : authorities.entrySet()) {
            if (!Role.isValid(entry.getValue())) {
                throw new GlobalException(ErrorStatus.INVALID_ROLE);
            }
        }
    }
}
