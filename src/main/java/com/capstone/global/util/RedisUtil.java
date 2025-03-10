package com.capstone.global.util;

import com.capstone.domain.document.entity.Document;
import com.capstone.domain.document.repository.DocumentRepository;
import com.capstone.domain.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Document> redisTemplate;
    private DocumentService documentService;
    private static final String CACHE_PREFIX = "DOC:";

    public Document loadFromCache(String key){
        Optional<Document> document = Optional.ofNullable(redisTemplate.opsForValue().get(key));
        if (document.isEmpty()){
            loadToCache(key);
            document = Optional.ofNullable(redisTemplate.opsForValue().get(key));
        }
        return document.orElse(null);
    }

    public void updateToCache(String key, Document newContent){
        //redisTemplate.delete(key);
        redisTemplate.opsForValue().set(CACHE_PREFIX + key, newContent);
    }

    public void loadToCache(String key){
        Document document = documentService.findDocumentById(key);
        redisTemplate.opsForValue().set(key, document);
    }
}
