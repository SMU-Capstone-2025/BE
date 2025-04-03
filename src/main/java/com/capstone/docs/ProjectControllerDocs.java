package com.capstone.docs;

import com.capstone.domain.project.dto.request.ProjectAuthorityRequest;
import com.capstone.domain.project.dto.request.ProjectSaveRequest;
import com.capstone.domain.project.entity.Project;
import com.capstone.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "프로젝트 관련 API")
public interface ProjectControllerDocs {

    @Operation(description = "신규 프로젝트 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(responseCode = "401", description = "인증 에러")

    })
    void registerProject(@RequestBody ProjectSaveRequest projectSaveRequest);

    @Operation(description = "프로젝트 업데이트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공"),
            @ApiResponse(responseCode = "401", description = "인증 에러"),
            @ApiResponse(responseCode = "500", description = "서버 에러")

    })
    void updateProject(@RequestBody ProjectSaveRequest projectSaveRequest);

    @Operation(description = "프로젝트 내 권한 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "권한 변경 성공"),
            @ApiResponse(responseCode = "401", description = "인증 에러"),
            @ApiResponse(responseCode = "500", description = "서버 에러")

    })
    void updateAuthority(@RequestBody ProjectAuthorityRequest projectAuthorityRequest);

    @Operation(description = "프로젝트에 신규 인원 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공"),
            @ApiResponse(responseCode = "401", description = "인증 에러"),
            @ApiResponse(responseCode = "500", description = "서버 에러")

    })
    void inviteProject(@RequestBody ProjectAuthorityRequest projectAuthorityRequest);

    @Operation(description = "사용자가 참여 중인 프로젝트 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공"),
            @ApiResponse(responseCode = "401", description = "인증 에러"),
            @ApiResponse(responseCode = "500", description = "서버 에러")

    })
    ResponseEntity<Project> loadProject(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam String projectId);
}
