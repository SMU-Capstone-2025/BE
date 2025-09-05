package com.capstone.domain.document.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record DocumentEditRequest(
        @NotBlank(message = "documentId는 필수입니다.")
        String documentId,
        @NotNull(message = "message는 필수입니다.")
        DocumentEditVo message
) {}
