package com.capstone.docs;

import com.capstone.domain.auth.login.dto.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "로그인 관련 API")
public interface LoginControllerDocs {

    @Operation(summary = "로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<String> doLogin();

//    @Operation(summary = "csrf 토큰 발급", description = "로그인 전 본 api 호출 후 csrf 토큰 발급")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "토큰 발급 성공"),
//            @ApiResponse(responseCode = "401", description = "인증 실패"),
//            @ApiResponse(responseCode = "500", description = "서버 에러")
//    })
//    String getCsrfToken(HttpServletRequest request);

}
