package com.capstone.domain.task.repository.custom;

import com.capstone.domain.task.dto.request.TaskRequest;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;
import com.capstone.domain.task.exception.VersionNotFoundException;
import com.capstone.domain.task.message.TaskMessages;
import com.capstone.global.util.DateUtil;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;


@RequiredArgsConstructor
public class CustomTaskRepositoryImpl implements CustomTaskRepository{
    private final MongoTemplate mongoTemplate;

    @Override
    public Version findByTaskIdAndVersion(String taskId, String version) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(taskId)); // Task ID 조건
        Task task = mongoTemplate.findOne(query, Task.class);

        if (task != null && task.getVersionHistory() != null) {
            return task.getVersionHistory().stream()
                    .filter(v -> v.getVersion().equals(version))
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }

    @Override
    public String modifyVersion(TaskRequest taskDto){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(taskDto.id())
                .and("versionHistory.version").is(taskDto.version()));

        Update update = new Update();
        update.set("versionHistory.$.modifiedBy", taskDto.modifiedBy());
        update.set("versionHistory.$.content", taskDto.content());
        update.set("versionHistory.$.summary", taskDto.summary());
        update.set("versionHistory.$.modifiedDateTime", DateUtil.getCurrentFormattedDateTime());

        UpdateResult result = mongoTemplate.updateFirst(query, update, Task.class);

        if (result.getMatchedCount() == 0) {
            throw new VersionNotFoundException(TaskMessages.VERSION_NOT_FOUND);
        }

        return TaskMessages.VERSION_MODIFIED;
    }
    @Override
    public List<Task> findByIds(List<String> taskIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(taskIds));
        return mongoTemplate.find(query, Task.class);
    }
}
