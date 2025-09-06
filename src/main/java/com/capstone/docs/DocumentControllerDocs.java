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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(summary = "문서 변경 로그 조회", description = "문서 ID 기준 변경 로그를 생성일자 최신순으로 페이지네이션하여 반환합니다. 페이지는 1부터 시작합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "성공 응답",
                                    value = """
                {
                  "success": true,
                  "code": "COMMON_200",
                  "message": "성공입니다.",
                  "result": {
                    "content": [
                      {
                        "oldContent": { /* 필드들 */ },
                        "newContent": { /* 필드들 */ },
                        "createdAt": "2025-09-06 22:58"
                      }
                    ],
                    "page": 1,
                    "size": 5,
                    "totalElements": 30,
                    "totalPages": 6
                  }
                }
                """
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증 실패 응답",
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
            @ApiResponse(responseCode = "403", description = "권한 부족",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "권한 부족 응답",
                                    value = """
                {
                  "success": false,
                  "code": "COMMON_403",
                  "message": "접근 권한이 없습니다."
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
    ResponseEntity<com.capstone.global.response.ApiResponse<
            com.capstone.global.response.PageResponse<java.util.List<com.capstone.domain.document.dto.DocumentLogDto>>
            >> getDocumentLogs(
            @PathVariable("documentId") String documentId,
            @RequestParam(value = "page", defaultValue = "1") @Valid @Min(1) int page,
            @RequestParam(value = "size", defaultValue = "5") @Valid @Min(1) int size
    );

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
    ResponseEntity<com.capstone.global.response.ApiResponse<Void>> deleteDocument(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("documentId") String documentId);

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
    ResponseEntity<com.capstone.global.response.ApiResponse<Void>> postDocument(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                                @RequestBody DocumentCreateRequest documentCreateRequest);


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
