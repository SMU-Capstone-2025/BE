package com.capstone.docs;

import com.capstone.domain.project.dto.request.ProjectAuthorityRequest;
import com.capstone.domain.project.dto.request.ProjectInviteRequest;
import com.capstone.domain.project.dto.request.ProjectSaveRequest;
import com.capstone.domain.project.dto.request.ProjectUpdateRequest;
import com.capstone.domain.project.dto.response.ProjectResponse;
import com.capstone.domain.project.entity.Project;
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

@Tag(name = "프로젝트 관련 API")
public interface ProjectControllerDocs {

    @Operation(description = "신규 프로젝트 생성")
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
            )

    })
    ResponseEntity<com.capstone.global.response.ApiResponse<Project>> registerProject(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ProjectSaveRequest projectSaveRequest);

    @Operation(description = "프로젝트 업데이트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공"),
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
    ResponseEntity<com.capstone.global.response.ApiResponse<Project>> updateProject(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("projectId") String projectId,
            @RequestBody ProjectUpdateRequest projectUpdateRequest);

    @Operation(description = "프로젝트 내 권한 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "권한 변경 성공"),
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
    ResponseEntity<com.capstone.global.response.ApiResponse<Project>> updateAuthority(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable String projectId,
            @RequestBody ProjectAuthorityRequest projectAuthority);

    @Operation(description = "프로젝트에 신규 인원 추가")
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
    ResponseEntity<com.capstone.global.response.ApiResponse<Void>> inviteProject(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable String projectId,
            @RequestBody ProjectInviteRequest projectInviteRequest);

    @Operation(description = "프로젝트의 세부 내용 불러오기")
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
    ResponseEntity<com.capstone.global.response.ApiResponse<ProjectResponse>> loadProject(@RequestParam String projectId);

    @Operation(description = "사용자가 참여 중인 프로젝트 목록 불러오기")
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
    ResponseEntity<com.capstone.global.response.ApiResponse<List<ProjectResponse>>> loadProjectList(@AuthenticationPrincipal CustomUserDetails userDetails);


    @Operation(description = "MANAGER 권한이 있는 프로젝트 유저가 다른 프로젝트 유저 강퇴하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강퇴 성공"),
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
    ResponseEntity<com.capstone.global.response.ApiResponse<Void>> deleteProjectUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable String projectId,
            @PathVariable String email);

}
