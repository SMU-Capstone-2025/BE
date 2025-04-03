package com.capstone.docs;

import com.capstone.domain.payment.dto.PaymentRequestDto;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "결제 관련 API")
public interface PaymentControllerDocs {

    @Operation(summary = "아임포트 결제 검증", description = "아임포트 결제 정보를 검증하고 유저 멤버십 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 검증 성공"),
            @ApiResponse(responseCode = "400", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<?> validateIamport(@RequestHeader("Authorization") String token, @RequestBody PaymentRequestDto request);
}