package com.capstone.domain.document.service;

import com.capstone.domain.document.entity.Document;
import com.capstone.domain.document.exception.DocumentNotFoundException;
import com.capstone.domain.document.message.DocumentMessage;
import com.capstone.domain.document.repository.DocumentRepository;
import com.capstone.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final RedisUtil redisUtil;

    public Document findDocumentCacheFirst(String key){
        return redisUtil.loadFromCache(key);
    }

    public void updateDocumentToCache(String key, String changes){
        Document oldDocument = new Document(key, changes);
        oldDocument.setContent(changes);
        redisUtil.updateToCache(key, oldDocument);

        System.out.println(oldDocument.getId());
        System.out.println(oldDocument.getContent());

        documentRepository.save(oldDocument);
    }

    public Document findDocumentById(String id){
        return documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(DocumentMessage.DOCUMENT_NOT_FOUND));
    }
}
