package com.capstone.docs;

import com.capstone.domain.document.dto.DocumentCreateRequest;
import com.capstone.domain.document.dto.DocumentEditRequest;
import com.capstone.domain.document.dto.DocumentResponse;
import com.capstone.domain.document.entity.Document;
import com.capstone.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Tag(name = "문서 관련 API")
public interface DocumentControllerDocs {

    @Operation(description = "문서 Id로 문서 정보를 불러와 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "서버 에러 응답",
                                    summary = "예상치 못한 서버 에러",
                                    value = """
                {
                  "success": false,
                  "code": "COMMON_500",
                  "message": "서버 에러, 관리자에게 문의 바랍니다."
                }
                """
                            )
                    )
            )
    })
    ResponseEntity<com.capstone.global.response.ApiResponse<DocumentResponse>> getDocument(@RequestParam("documentId") String documentId);

    @Operation(description = "문서 Id로 문서 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증 실패 응답",
                                    summary = "유효하지 않은 토큰 또는 로그인 필요",
                                    value = """
                    {
                      "success": false,
                      "code": "COMMON_401",
                      "message": "인증이 필요합니다."
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "서버 에러 응답",
                                    summary = "예상치 못한 서버 에러",
                                    value = """
                {
                  "success": false,
                  "code": "COMMON_500",
                  "message": "서버 에러, 관리자에게 문의 바랍니다."
                }
                """
                            )
                    )
            )
    })
    ResponseEntity<com.capstone.global.response.ApiResponse<Void>> deleteDocument(@RequestParam("documentId") String documentId);

    @Operation(description = "문서 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증 실패 응답",
                                    summary = "유효하지 않은 토큰 또는 로그인 필요",
                                    value = """
                    {
                      "success": false,
                      "code": "COMMON_401",
                      "message": "인증이 필요합니다."
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "서버 에러 응답",
                                    summary = "예상치 못한 서버 에러",
                                    value = """
                {
                  "success": false,
                  "code": "COMMON_500",
                  "message": "서버 에러, 관리자에게 문의 바랍니다."
                }
                """
                            )
                    )
            )
    })
    ResponseEntity<com.capstone.global.response.ApiResponse<Void>> postDocument(@RequestBody DocumentCreateRequest documentCreateRequest);


    @Operation(description = "프론트 단에서 /pub/editing 으로 send 하면, MessageMapping이 동작함.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 전송 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "서버 에러 응답",
                                    summary = "예상치 못한 서버 에러",
                                    value = """
                {
                  "success": false,
                  "code": "COMMON_500",
                  "message": "서버 에러, 관리자에게 문의 바랍니다."
                }
                """
                            )
                    )
            )
    })
    void sendMessage(DocumentEditRequest params, @Header("simpSessionAttributes") Map<String, Object> sessionAttributes);


    @Operation(description = "프로젝트의 문서 리스트 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "서버 에러 응답",
                                    summary = "예상치 못한 서버 에러",
                                    value = """
                {
                  "success": false,
                  "code": "COMMON_500",
                  "message": "서버 에러, 관리자에게 문의 바랍니다."
                }
                """
                            )
                    )
            )

    })
    ResponseEntity<com.capstone.global.response.ApiResponse<List<Document>>> getDocumentList(@RequestParam("projectId") String projectId);

    @Operation(description = "문서의 상태 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변경 성공"),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증 실패 응답",
                                    summary = "유효하지 않은 토큰 또는 로그인 필요",
                                    value = """
                        {
                          "success": false,
                          "code": "COMMON_401",
                          "message": "인증이 필요합니다."
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "서버 에러 응답",
                                    summary = "예상치 못한 서버 에러",
                                    value = """
                {
                  "success": false,
                  "code": "COMMON_500",
                  "message": "서버 에러, 관리자에게 문의 바랍니다."
                }
                """
                            )
                    )
            )

    })
    ResponseEntity<com.capstone.global.response.ApiResponse<Document>> putStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String documentId,
            @RequestParam String status);




    @Operation(description = "프로젝트의 문서 리스트 생성시간 기준 오름차순 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "서버 에러 응답",
                                    summary = "예상치 못한 서버 에러",
                                    value = """
                {
                  "success": false,
                  "code": "COMMON_500",
                  "message": "서버 에러, 관리자에게 문의 바랍니다."
                }
                """
                            )
                    )
            )

    })
    ResponseEntity<com.capstone.global.response.ApiResponse<List<Document>>> getDocumentListSortedByCreateAt(
            @RequestParam("projectId") String projectId);
}
