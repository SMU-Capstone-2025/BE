package com.capstone.docs;


import com.capstone.domain.AI.dto.AIRequest;
import com.capstone.domain.mypage.dto.CalendarTaskDto;
import com.capstone.domain.mypage.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Tag(name = "마이페이지 서비스 API")
public interface MypageControllerDocs
{
    @Operation(summary = "유저 정보 조회", description = "토큰을 이용해 유저 정보 획득")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 반환 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<UserDto.UserInfoDto> loadUser(@RequestHeader("Authorization") String accessToken);

    @Operation(summary = "비밀번호 재설정", description = "현재비밀번호, 새로운 비밀번호, 검증 비밀번호(새로운 비밀번호와 일치하는지)를 입력해 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "바밀번호 변경 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<Void> newPassword(@RequestHeader("Authorization") String accessToken,
                                     @RequestBody UserDto.UserPasswordDto userPasswordDto);

    @Operation(summary = "프로필 사진 변경", description = "사용자 프로필 사진을 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 변경 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<Void> newProfile(@RequestHeader("Authorization") String accessToken,
                                    @RequestBody UserDto.UserProfileDto userProfileDto);


    @Operation(summary = "이메일 변경 검증", description = "변경될 이메일로 인증번호 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 번호 반환, 사용자의 입력과 비교하여 인증"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    public ResponseEntity<String> checkEmail(@RequestParam String email) throws Exception;

    @Operation(summary = "이메일 변경", description = "현재 이메일, 새로운 이메일을 입력받아 이메일 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 변경 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<Void> newEmail(@RequestHeader("Authorization") String accessToken,
                                  @RequestBody UserDto.UserEmailDto userEmailDto) throws Exception;

    @Operation(summary = "회원탈퇴", description = "사용자 계정 삭제 -> 연결된 프로젝트,작업에는 (알수없음)으로 표시 됨")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "계정 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String accessToken);

    @Operation(summary = "사용자 작업 정보", description = "캘린더에 쓸 사용자 작업 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작업 반환 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<List<CalendarTaskDto>> getTasks(@RequestHeader("Authorization") String accessToken);
}
