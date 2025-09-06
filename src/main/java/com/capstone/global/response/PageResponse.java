package com.capstone.global.response;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public record PageResponse<T>(
        T content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <T> PageResponse<List<T>> of(Page<T> content, Pageable pageable) {
        return new PageResponse<>(content.getContent(), pageable.getPageNumber() + 1, pageable.getPageSize(), content.getTotalElements(), content.getTotalPages());
    }

    public static <T> PageResponse<T> empty(Pageable pageable) {
        return new PageResponse<>(null, pageable.getPageNumber() + 1, pageable.getPageSize(), 0, 0);
    }
}