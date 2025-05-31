package com.capstone.domain.document.service;

import com.capstone.domain.document.dto.DocumentCreateRequest;
import com.capstone.domain.document.dto.DocumentEditVo;
import com.capstone.domain.document.dto.DocumentResponse;
import com.capstone.domain.document.entity.Document;
import com.capstone.domain.document.message.DocumentStatus;
import com.capstone.domain.document.repository.DocumentRepository;
import com.capstone.domain.project.entity.Project;
import com.capstone.domain.project.repository.ProjectRepository;
import com.capstone.domain.task.dto.response.TaskResponse;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.message.TaskStatus;
import com.capstone.global.kafka.service.KafkaProducerService;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;
import com.capstone.global.security.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaProducerService kafkaProducerService;

    @Cacheable(value = "document", key = "'DOC:loaded' + #key", unless = "#result == null")
    public DocumentResponse findDocumentCacheFirst(String key){
        Document doc = Optional.ofNullable(documentRepository.findDocumentByDocumentId(key))
                .orElseThrow(() -> new GlobalException(ErrorStatus.DOCUMENT_NOT_FOUND));
        return DocumentResponse.from(doc);
    }

    public List<Document> findDocumentList(String projectId){
        return documentRepository.findDocumentsByProjectId(projectId);

    }

    public void updateDocumentToCache(String key, DocumentEditVo changes){
        Document doc = documentRepository.findDocumentByDocumentId(key);
        doc.update(key, doc.getProjectId(), changes);
        redisTemplate.opsForValue().set("DOC:waited:" + key, doc, 1, TimeUnit.HOURS);
    }

    public Document deleteDocumentFromCacheAndDB(String key){
        Document document = documentRepository.findDocumentByDocumentId(key);
        redisTemplate.delete(key);
        documentRepository.delete(document);
        return document;
    }

    public Document createDocument(DocumentCreateRequest documentCreateRequest){
        return documentRepository.save(documentCreateRequest.to());
    }


    // 별도 변환과정 없이 바로 save 하게 되면 자료형이 LinkedHashMap이라 바꿔줘야 함.
    public Document mapToDocument(Object data) {
        if (data instanceof Map) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                String json = objectMapper.writeValueAsString(data);
                return objectMapper.readValue(json, Document.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Transactional
    public Document updateStatus(String id, String status, CustomUserDetails userDetails){
        validateStatus(status);

        Document document = documentRepository.findDocumentByDocumentId(id);
        if (document == null) {
            throw new GlobalException(ErrorStatus.DOCUMENT_NOT_FOUND);
        }
        document.setStatus(status);
        documentRepository.save(document);
        kafkaProducerService.sendDocumentEvent("document.changed", "UPDATE", DocumentResponse.from(document), userDetails.getEmail());

        return document;
    }
    public void validateStatus(String status){
        if (!DocumentStatus.isValid(status)){
            throw new GlobalException(ErrorStatus.INVALID_STATUS);
        }
    }

    public List<Document> findDocumentListSortedByDateAsc(String projectId)
    {
        return documentRepository.findDocumentsByProjectIdOrderByCreatedAt(projectId);
    }

    @Scheduled(fixedRate = 5000) // 5초마다 실행
    @Transactional
    public void syncToMongoDB() {
        Set<String> keys = redisTemplate.keys("DOC:waited:*");

        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                Object data = redisTemplate.opsForValue().get(key);

                if (data != null) {
                    Document document = mapToDocument(data);


                    if (document != null) {
                        documentRepository.save(document);

                        // 3. 저장 후 Redis에서 키 이름 변경 (waited → loaded)
                        String newKey = key.replace("DOC:waited:", "DOC:loaded:");
                        redisTemplate.rename(key, newKey);

                    }
                }
            }
        }
    }

}
