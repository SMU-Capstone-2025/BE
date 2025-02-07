package com.capstone.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityRequest {
    private String projectId;
    private Map<String, String> authorities;

    public List<String> getAuthorityKeysAsList() {
        return authorities == null ? List.of() : authorities.keySet().stream().collect(Collectors.toList());
    }
}
