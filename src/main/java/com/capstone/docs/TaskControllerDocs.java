package com.capstone.docs;

import com.capstone.domain.log.entity.LogEntity;
import com.capstone.domain.task.dto.request.TaskRequest;
import com.capstone.domain.task.dto.response.TaskResponse;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;
import com.capstone.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "작업 관련 API")
public interface TaskControllerDocs {

    @Operation(description = "신규 작업 생성")
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
    ResponseEntity<com.capstone.global.response.ApiResponse<Task>> postTask(@RequestBody TaskRequest taskDto);

    @Operation(description = "작업의 세부 내용 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공"),
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
    ResponseEntity<com.capstone.global.response.ApiResponse<Version>> getTask(@RequestParam String taskId);

    @Operation(description = "작업 삭제")
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
    ResponseEntity<com.capstone.global.response.ApiResponse<Task>> deleteTask(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String id);

    @Operation(description = "작업의 상태 변경")
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
    ResponseEntity<com.capstone.global.response.ApiResponse<Task>> putStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String id,
            @RequestParam String status);

    @Operation(description = "작업 내 신규 버전 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공"),
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
    ResponseEntity<com.capstone.global.response.ApiResponse<Version>> postVersion(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody TaskRequest taskDto,
                                       @RequestParam(value = "fileId", required = false) String fileId);

    @Operation(description = "작업 내 버전 목록 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
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
    ResponseEntity<com.capstone.global.response.ApiResponse<List<Version>>> getVersionLists(@RequestParam String taskId);

    @Operation(description = "버전 롤백")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "롤백 성공"),
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
    ResponseEntity<com.capstone.global.response.ApiResponse<TaskResponse>> getVersionRollback(@RequestParam String taskId, @RequestParam String version);



    @Operation(description = "작업을 deadline을 기준으로 정렬")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정렬 성공"),
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
    ResponseEntity<com.capstone.global.response.ApiResponse<List<Task>>> getDeadlineList(@AuthenticationPrincipal CustomUserDetails userDetails);
}
