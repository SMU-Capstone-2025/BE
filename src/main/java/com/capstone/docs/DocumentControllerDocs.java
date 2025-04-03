package com.capstone.docs;

import com.capstone.domain.document.dto.DocumentEditRequest;
import com.capstone.domain.document.dto.DocumentEditResponse;
import com.capstone.domain.document.entity.Document;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Tag(name = "문서 관련 API")
public interface DocumentControllerDocs {

    @Operation(description = "문서 Id로 문서 정보를 불러와 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    Document getDocument(@RequestParam("documentId") String documentId);

    @Operation(description = "프론트 단에서 /pub/editing 으로 send 하면, MessageMapping이 동작함.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 전송 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    void sendMessage(DocumentEditRequest params, @Header("simpSessionAttributes") Map<String, Object> sessionAttributes);
}
