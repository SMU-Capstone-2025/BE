package com.capstone.domain.file.service;

import com.capstone.domain.file.FileMessages;
import com.capstone.domain.file.FileTypes;
import com.capstone.domain.file.exception.InvalidFileException;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {
    public final GridFsTemplate gridFsTemplate;

    public String upload(MultipartFile file) throws IOException {
        ObjectId objectId;
        if(!FileTypes.SUPPORTED_TYPES(file.getContentType())){
            throw new InvalidFileException(FileMessages.FILE_NOT_SUPPORTED);
        }

        if(file.isEmpty()){
            throw new InvalidFileException(FileMessages.FILE_EMPTY);
        }

        objectId = gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType()
        );

        return objectId.toHexString();
    }

    public ResponseEntity<Resource> download(String fileId) {
        GridFSFile file = gridFsTemplate.findOne(
                new Query(Criteria.where("_id").is(fileId))
        );


        GridFsResource resource = gridFsTemplate.getResource(file);

        if (!resource.exists()){
            throw new InvalidFileException(FileMessages.FILE_NOT_FOUND);
        }

        String contentType = file.getMetadata() != null && file.getMetadata().containsKey("_contentType")
                ? file.getMetadata().get("_contentType").toString()
                : "application/octet-stream";

        String filename = file.getFilename();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    public void delete(String fileId) {
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(fileId)));
    }
}
