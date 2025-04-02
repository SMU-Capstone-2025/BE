package com.capstone.domain.auth.docs;

import com.capstone.domain.auth.register.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "회원가입 관련 API")
public interface RegisterControllerDocs {

    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest);

    @Operation(summary = "이메일 사용 가능 여부 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 사용 가능 여부 반환"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<Boolean> checkEmailAvailable(@RequestParam String email);

    @Operation(summary = "사용 가능한 이메일로 인증 번호 전송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 번호 반환, 사용자의 입력과 비교하여 인증"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<String> sendMailConfirm(@RequestParam String email) throws Exception;
}
