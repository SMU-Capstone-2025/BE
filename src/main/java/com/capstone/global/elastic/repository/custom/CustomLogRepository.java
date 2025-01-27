package com.capstone.global.elastic.repository.custom;

import com.capstone.global.elastic.entity.LogEntity;

import java.util.List;

public interface CustomLogRepository {
    List<LogEntity> findAllByTaskId(String taskId);
    //List<LogEntity> findAllByTaskIdAndMethod(String taskId, String method);
}
