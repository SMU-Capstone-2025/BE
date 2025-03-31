package com.capstone.domain.task.repository.custom;

import com.capstone.domain.task.dto.request.TaskRequest;
import com.capstone.domain.task.entity.Version;

public interface CustomTaskRepository {
    Version findByTaskIdAndVersion(String taskId, String version);
    String modifyVersion(TaskRequest taskDto);
}
