package com.capstone.domain.file.service;

import com.capstone.domain.file.FileTypes;
import com.capstone.global.image.ImageProcessingService;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.capstone.domain.file.FileTypes.*;

@Service
@RequiredArgsConstructor
public class FileService {
    public final GridFsTemplate gridFsTemplate;
    public final ImageProcessingService imageProcessingService;
    // TODO: 파일 형식 별 처리 로직이 다르다면 모듈화 해서 처리하는 걸로.

    public String uploadFile(MultipartFile file) throws Exception {
        String contentType = file.getContentType();
        ObjectId objectId;
        if (contentType == null) {
            throw new IllegalArgumentException("파일의 MIME 타입을 확인할 수 없습니다.");
        }

        InputStream inputStream = file.getInputStream();
        GridFSUploadOptions options = new GridFSUploadOptions().metadata(new org.bson.Document("contentType", contentType));

        // 이미지 파일인 경우
        if (SUPPORTED_IMAGE_TYPES.contains(contentType)) {
            objectId = gridFsTemplate.store(
                    file.getInputStream(),
                    file.getOriginalFilename(),
                    file.getContentType()
            );
        }

        /*// 문서 파일인 경우
        else if (SUPPORTED_DOCUMENT_TYPES.contains(contentType)) {
        }
        // 엑셀 파일인 경우
        else if (SUPPORTED_SPREADSHEET_TYPES.contains(contentType)) {
        }*/

        else {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다: " + contentType);
        }

        return objectId.toHexString();
    }

    public ResponseEntity<Resource> downloadFile(String fileId) throws IOException {
        GridFSFile file = gridFsTemplate.findOne(
                new Query(Criteria.where("_id").is(fileId))
        );

        GridFsResource resource = gridFsTemplate.getResource(file);

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


    public void deleteFile(String fileId) {
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(fileId)));
    }
}
