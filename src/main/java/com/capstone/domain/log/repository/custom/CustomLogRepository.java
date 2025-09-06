package com.capstone.domain.log.repository.custom;

import com.capstone.domain.document.dto.DocumentLogDto;
import com.capstone.domain.log.entity.LogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomLogRepository {
    List<LogEntity> findAllByTaskId(String taskId);
    Page<DocumentLogDto> findAllByDocumentId(String documentId, Pageable pageable);
    //List<LogEntity> findAllByTaskIdAndMethod(String taskId, String method);
}
