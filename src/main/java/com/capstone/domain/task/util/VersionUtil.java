package com.capstone.domain.task.util;

import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;

public class VersionUtil {

    public static Version getCurrentVersionEntity(Task task){
        return task.getVersionHistory().stream()
                .filter(v -> v.getVersion().equals(task.getCurrentVersion()))
                .findFirst()
                .orElseThrow(() -> new GlobalException(ErrorStatus.VERSION_NOT_FOUND));

    }
}
