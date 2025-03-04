package task.repository.custom;

import task.dto.TaskDto;
import task.entity.Version;

public interface CustomTaskRepository {
    Version findByTaskIdAndVersion(String taskId, String version);
    String modifyVersion(TaskDto taskDto);
}
