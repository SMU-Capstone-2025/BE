package com.capstone.docs;

import com.capstone.domain.notification.entity.Notification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "알림 관련 API")
public interface NotificationControllerDocs {

    @Operation(description = "토큰을 통해 사용자를 인식하여 알림 목록 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "401", description = "인증 에러"),
            @ApiResponse(responseCode = "500", description = "서버 에러")

    })
    ResponseEntity<List<Notification>> getNotificationLists(@RequestHeader("Authorization") String token);

    @Operation(description = "알림을 읽어 알림의 상태 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "401", description = "인증 에러"),
            @ApiResponse(responseCode = "500", description = "서버 에러")

    })
    ResponseEntity<String> readNotification(@RequestParam("notificationId") String notificationId);
}
