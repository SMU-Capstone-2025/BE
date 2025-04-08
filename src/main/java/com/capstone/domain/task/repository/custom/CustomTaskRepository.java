package com.capstone.domain.task.repository.custom;

import com.capstone.domain.task.dto.request.TaskRequest;

import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;

import java.util.List;

public interface CustomTaskRepository {
    Version findByTaskIdAndVersion(String taskId, String version);
    String modifyVersion(TaskRequest taskDto);
    List<Task> findByIds(List<String> taskIds);
    List<Task> findByUserEmailAndSortDeadLine(String email);
}
