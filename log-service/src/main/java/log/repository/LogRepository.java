package log.repository;

import log.entity.LogEntity;
import log.repository.custom.CustomLogRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface LogRepository extends ElasticsearchRepository<LogEntity, String>, CustomLogRepository {
}