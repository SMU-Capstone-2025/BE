package com.capstone.domain.project.dto.request;

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
}
