package com.capstone.domain.document.service;

import com.capstone.domain.document.dto.*;
import com.capstone.domain.document.entity.Document;
import com.capstone.domain.document.message.DocumentStatus;
import com.capstone.domain.document.repository.DocumentRepository;

import com.capstone.global.kafka.dto.DocumentChangePayload;
import com.capstone.global.kafka.dto.detail.DocumentChangeDetail;
import com.capstone.global.kafka.service.KafkaProducerService;
import com.capstone.global.kafka.topic.KafkaEventTopic;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;
import com.capstone.global.security.CustomUserDetails;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final ObjectMapper objectMapper;


    public DocumentResponse findDocumentCacheFirst(String key){
        Document doc = Optional.ofNullable(documentRepository.findDocumentByDocumentId(key))
                .orElseThrow(() -> new GlobalException(ErrorStatus.DOCUMENT_NOT_FOUND));
        return DocumentResponse.from(doc);
    }

    public List<Document> findDocumentList(String projectId){
        return documentRepository.findDocumentsByProjectId(projectId);

    }

    public void updateDocumentEditStatus(DocumentEditVo documentEditVo){
        String key = "DOC:editing:" + documentEditVo.getDocumentId();
        DocumentCursorDto dto = new DocumentCursorDto(documentEditVo.getUser().getUserName(), documentEditVo.getCursor());

        redisTemplate.opsForHash().put(key, documentEditVo.getUser().getUserEmail(), dto);
    }

    public List<DocumentCursorDto> findOtherUsersCursor(String documentId) {
        String key = "DOC:editing:" + documentId;
        Object all = redisTemplate.opsForHash().entries(key);

        return mapToDocumentCursor(all, objectMapper);
    }

    public void updateDocumentToCache(String email, String key, DocumentEditVo changes){
        Document doc = documentRepository.findDocumentByDocumentId(key);
        doc.update(email, key, doc.getProjectId(), changes);
        DocumentWrapper tempDto = DocumentWrapper.toDto(doc, email);
        redisTemplate.opsForValue().set("DOC:waited:" + key, tempDto, 10, TimeUnit.SECONDS);
    }

    public void deleteDocumentFromCacheAndDB(CustomUserDetails customUserDetails, String key){
        Document document = documentRepository.findDocumentByDocumentId(key);
        redisTemplate.delete(key);
        documentRepository.delete(document);
        kafkaProducerService.sendEvent(KafkaEventTopic.DOCUMENT_DELETED, DocumentChangePayload.from(document
                , null, null, customUserDetails.getEmail(), document.getEditors()));
    }

    public void createDocument(CustomUserDetails customUserDetails, DocumentCreateRequest documentCreateRequest){
        String editorId = customUserDetails.getEmail();
        documentRepository.save(documentCreateRequest.to(editorId));
    }


    // 별도 변환과정 없이 바로 save 하게 되면 자료형이 LinkedHashMap이라 바꿔줘야 함.
    public DocumentWrapper mapToDocument(Object data) {
        if (data instanceof Map) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                String json = objectMapper.writeValueAsString(data);
                return objectMapper.readValue(json, DocumentWrapper.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<DocumentCursorDto> mapToDocumentCursor(Object data, ObjectMapper mapper) {
        if (!(data instanceof Map<?, ?> m) || m.isEmpty()) return List.of();
        return m.values().stream()
                .map(v -> mapper.convertValue(v, DocumentCursorDto.class))
                .toList();
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
//        kafkaProducerService.sendEvent(KafkaEventTopic.DOCUMENT_UPDATED, DocumentChangePayload.from(document
//                , null, null, userDetails.getEmail(), document.getEditors()));

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

        if (!keys.isEmpty()) {
            for (String key : keys) {
                Object data = redisTemplate.opsForValue().get(key);

                if (data != null) {
                    DocumentWrapper documentWrapper = mapToDocument(data);
                    Document document = documentWrapper.document();
                    String documentEditor = documentWrapper.editor();

                    Document oldDocument = documentRepository.findById(document.getId()).orElseThrow();

                    DocumentChangeDetail oldContent = DocumentChangeDetail.from(oldDocument);
                    DocumentChangeDetail newContent = DocumentChangeDetail.from(document);
                    documentRepository.save(document);

                    redisTemplate.delete(key);

                    kafkaProducerService.sendEvent(KafkaEventTopic.DOCUMENT_UPDATED, DocumentChangePayload.from(document
                            , oldContent, newContent, documentEditor, document.getEditors()));

                }
            }
        }
    }

}
