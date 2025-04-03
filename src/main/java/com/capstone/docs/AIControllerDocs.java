package com.capstone.docs;

import com.capstone.domain.AI.dto.AIRequest;
import com.capstone.domain.AI.dto.AIReviseRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Tag(name = "AI 서비스 API")
public interface AIControllerDocs {

    @Operation(summary = "문법 수정", description = "입력된 텍스트의 문법을 수정하여 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정된 문서 반환 성공"),
            @ApiResponse(responseCode = "401", description = "인증 에러"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<Map<String, Object>> ModifyGrammar (@RequestHeader("Authorization") String token, @RequestBody AIRequest aiRequest);

    @Operation(summary = "문서 요약", description = "입력된 문서를 요약하여 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요약된 문서 반환 성공"),
            @ApiResponse(responseCode = "401", description = "인증 에러"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<Map<String, Object>>ModifyDocument (@RequestHeader("Authorization") String token,@RequestBody AIRequest aiRequest);

    @Operation(summary = "요약 미리보기", description = "입력된 문서의 요약을 미리보기로 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미리보기 반환 성공"),
            @ApiResponse(responseCode = "401", description = "인증 에러"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<Map<String, Object>> previewSummary(@RequestHeader("Authorization") String token,@RequestParam AIRequest aiRequest);

    @Operation(summary = "요약 재수정", description = "AI가 응답해준 내용이 마음에 들지 않으면 응답내용과 피드백을 보내 다시 수정해줌")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정된 요약 반환 성공"),
            @ApiResponse(responseCode = "401", description = "인증 에러"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    public ResponseEntity<Map<String, Object>> reviseSummary(@RequestHeader("Authorization") String token, @RequestBody AIReviseRequest request);
}

