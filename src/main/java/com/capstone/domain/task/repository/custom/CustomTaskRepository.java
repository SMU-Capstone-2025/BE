package com.capstone.domain.task.repository.custom;

import com.capstone.domain.task.dto.TaskDto;
import com.capstone.domain.task.entity.Version;

import java.util.List;

public interface CustomTaskRepository {
    Version findByTaskIdAndVersion(String taskId, String version);
    String modifyVersion(TaskDto taskDto);
}
