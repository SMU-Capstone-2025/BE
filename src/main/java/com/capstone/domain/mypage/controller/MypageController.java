package com.capstone.domain.mypage.controller;



import com.capstone.docs.MypageControllerDocs;
import com.capstone.domain.mypage.dto.CalendarTaskDto;
import com.capstone.domain.mypage.dto.UserDto;
import com.capstone.domain.mypage.service.MypageService;

import com.capstone.global.mail.service.MailService;
import com.capstone.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//TODO: 화상회의 개발 후 캘린더에 추가

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController implements MypageControllerDocs
{
    private final MypageService mypageService;
    private final MailService mailService;

    //유저 정보
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserDto.UserInfoDto>> loadUser(@RequestHeader("Authorization") String accessToken)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.getUser(accessToken)));
    }

    //비밀번호 변경
    @PutMapping("/password/new")
    public ResponseEntity<ApiResponse<String>> newPassword(@RequestHeader("Authorization") String accessToken,
                                                           @RequestBody UserDto.UserPasswordDto userPasswordDto)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.modifyPassword(accessToken, userPasswordDto)));
    }
    //프로필 사진 변경
    @PutMapping("/profile/new")
    public ResponseEntity<ApiResponse<String>> newProfile(@RequestHeader("Authorization") String accessToken,
                                                          @RequestBody UserDto.UserProfileDto userProfileDto)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.modifyProfile(accessToken, userProfileDto)));
    }

    //이메일 변경
    //이메일 변경 후에는 강제 로그아웃 시킨 뒤 다시 로그인하게 해야 토큰 동작함
    @PutMapping("/email/new")
    public ResponseEntity<ApiResponse<String>> newEmail(@RequestHeader("Authorization") String accessToken,
                                                        @RequestBody UserDto.UserEmailDto userEmailDto) throws Exception {
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.modifyEmail(accessToken,userEmailDto)));
    }

    //이메일 변경 전 새로운 이메일 확인
    @PostMapping("/email/check")
    public ResponseEntity<ApiResponse<String>> checkEmail(@RequestParam String email) throws Exception
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(mailService.sendSimpleMessageForNewEmail(email)));
    }

    //계정 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteUser(@RequestHeader("Authorization") String accessToken)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.removeUser(accessToken)));
    }

    @GetMapping("/calendar/events/task")
    public ResponseEntity<ApiResponse<List<CalendarTaskDto>>> getTasks(@RequestHeader("Authorization") String accessToken)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.getUserTask(accessToken)));
    }


}
