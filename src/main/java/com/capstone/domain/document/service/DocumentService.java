package com.capstone.domain.document.service;

import com.capstone.domain.document.entity.Document;
import com.capstone.domain.document.exception.DocumentNotFoundException;
import com.capstone.domain.document.message.DocumentMessage;
import com.capstone.domain.document.repository.DocumentRepository;
import com.capstone.global.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final RedisUtil redisUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    @Cacheable(value = "document", key = "'DOC:loaded' + #key", unless = "#result == null")
    public Document findDocumentCacheFirst(String key){
        return documentRepository.findDocumentByDocumentId(key);
    }

    @CachePut(value = "document", key = "'DOC:waited:' + #key")
    public Document updateDocumentToCache(String key, String changes){
        Document updatedDocument = new Document(key, changes);
        documentRepository.save(updatedDocument);  // 먼저 DB에 저장
        return updatedDocument;  // 저장된 값이 캐시에도 반영됨
    }


    // 별도 변환과정 없이 바로 save 하게 되면 자료형이 LinkedHashMap이라 바꿔줘야 함.
    public Document mapToDocument(Object data) {
        if (data instanceof Map) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(data);
                return objectMapper.readValue(json, Document.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Scheduled(fixedRate = 3000) // 3초마다 실행
    @Transactional
    public void syncToMongoDB() {
        Set<String> keys = redisTemplate.keys("document::DOC:waited:*");

        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                Object data = redisTemplate.opsForValue().get(key);

                if (data != null) {
                    Document document = mapToDocument(data);

                    if (document != null) {
                        // 2. 문서에 UUID 할당 후 MongoDB 저장
                        document.setId(UUID.randomUUID().toString());
                        documentRepository.save(document);

                        // 3. 저장 후 Redis에서 키 이름 변경 (waited → loaded)
                        String newKey = key.replace("DOC:waited:", "DOC:loaded:");
                        redisTemplate.rename(key, newKey);

                    }
                }
            }
        } else {
            System.out.println("저장할 데이터 없음.");
        }
    }
}
