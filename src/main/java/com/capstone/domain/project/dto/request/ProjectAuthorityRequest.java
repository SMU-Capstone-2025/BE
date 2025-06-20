package com.capstone.domain.project.dto.request;

import com.capstone.domain.project.dto.query.ProjectUserAuthority;
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
        List<ProjectUserAuthority> authorities
) {
    public List<String> getAuthorityKeysAsList() {
        return authorities == null ? List.of() :
                authorities.stream()
                        .map(ProjectUserAuthority::getUserId)
                        .toList();
    }

    public List<ProjectUser> from() {
        return authorities == null ? List.of() :
                authorities.stream()
                        .map(auth -> ProjectUser.builder()
                                .projectId(projectId)
                                .userId(auth.getUserId())
                                .role(auth.getRole())
                                .status("ACCEPTED")
                                .joinedAt(LocalDate.now().toString())
                                .build())
                        .toList();
    }

    public void validateRoles() {
        if (authorities == null) return;

        for (ProjectUserAuthority auth : authorities) {
            if (!Role.isValid(auth.getRole())) {
                throw new GlobalException(ErrorStatus.INVALID_ROLE);
            }
        }
    }

    public String getRoleByUserId(String userId) {
        if (authorities == null) return null;

        return authorities.stream()
                .filter(auth -> auth.getUserId().equals(userId))
                .map(ProjectUserAuthority::getRole)
                .findFirst()
                .orElse(null);
    }
}
