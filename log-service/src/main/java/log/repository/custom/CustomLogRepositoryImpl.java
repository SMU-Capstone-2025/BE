package log.repository.custom;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import log.entity.LogEntity;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomLogRepositoryImpl implements CustomLogRepository{

    private final ElasticsearchOperations elasticsearchOperations;

    public CustomLogRepositoryImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public List<LogEntity> findAllByTaskId(String taskId) {
        // 새로운 쿼리 빌더를 사용하여 쿼리 생성
        Query query = QueryBuilders.term()
                .field("taskId")
                .value(taskId)
                .build()._toQuery();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .build();

        // Elasticsearch에서 검색 실행
        SearchHits<LogEntity> searchHits = elasticsearchOperations.search(nativeQuery, LogEntity.class);

        // 결과를 리스트로 변환하여 반환
        return searchHits.getSearchHits()
                .stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }
//    @Override
//    public List<LogEntity> findAllByTaskIdAndMethod(String taskId, String method){
//
//    }
}
