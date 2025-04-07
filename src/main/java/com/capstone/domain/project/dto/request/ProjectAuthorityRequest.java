package com.capstone.domain.project.dto.request;

import com.capstone.domain.user.entity.Role;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public record ProjectAuthorityRequest(
        String projectId,
        Map<String, String> authorities
)
{
    public List<String> getAuthorityKeysAsList() {
        return authorities == null ? List.of() : authorities.keySet().stream().collect(Collectors.toList());
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
