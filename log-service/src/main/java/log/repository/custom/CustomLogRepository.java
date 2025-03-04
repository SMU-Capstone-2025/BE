package log.repository.custom;


import log.entity.LogEntity;

import java.util.List;

public interface CustomLogRepository {
    List<LogEntity> findAllByTaskId(String taskId);
    //List<LogEntity> findAllByTaskIdAndMethod(String taskId, String method);
}
