package com.capstone.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    @NotBlank
    private String projectName;
    @NotBlank
    private String description;
    private List<String> invitedEmails;
}
