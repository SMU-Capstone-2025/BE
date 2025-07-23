package com.capstone.domain.mypage.controller;



import com.capstone.docs.MypageControllerDocs;
import com.capstone.domain.mypage.dto.CalendarTaskDto;
import com.capstone.domain.mypage.dto.EmailDto;
import com.capstone.domain.mypage.dto.UserDto;
import com.capstone.domain.mypage.service.MypageService;

import com.capstone.domain.mail.service.RegisterMailService;
import com.capstone.global.response.ApiResponse;
import com.capstone.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//TODO: 화상회의 개발 후 캘린더에 추가

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController implements MypageControllerDocs
{
    private final MypageService mypageService;
    private final RegisterMailService mailService;

    //유저 정보
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserDto.UserInfoDto>> loadUser(@AuthenticationPrincipal CustomUserDetails userDetails)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.getUser(userDetails)));
    }

    //비밀번호 변경
    @PutMapping("/password/new")
    public ResponseEntity<ApiResponse<String>> newPassword(@RequestBody UserDto.UserPasswordDto userPasswordDto)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.modifyPassword(userPasswordDto)));
    }
    //프로필 사진 변경
    @PutMapping("/profile/new")
    public ResponseEntity<ApiResponse<String>> newProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestBody UserDto.UserProfileDto userProfileDto)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.modifyProfile(userDetails, userProfileDto)));
    }

    //이메일 변경
    //이메일 변경 후에는 강제 로그아웃 시킨 뒤 다시 로그인하게 해야 토큰 동작함
    @PutMapping("/email/new")
    public ResponseEntity<ApiResponse<String>> newEmail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestBody UserDto.UserEmailDto userEmailDto) throws Exception {
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.modifyEmail(userDetails,userEmailDto)));
    }

    //이메일 변경 전 새로운 이메일 확인
    @PostMapping("/email/check")
    public ResponseEntity<ApiResponse<String>> checkEmail(@RequestBody EmailDto emailDto) throws Exception
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(mailService.sendSimpleMessage(emailDto.email())));
    }
    @GetMapping("/email/avail")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailAvailable(@RequestParam String name,
                                                                    @RequestParam String email){
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.checkEmail(name,email)));
    }

    //계정 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.removeUser(userDetails)));
    }

    @GetMapping("/calendar/events/task")
    public ResponseEntity<ApiResponse<List<CalendarTaskDto>>> getTasks(@AuthenticationPrincipal CustomUserDetails userDetails)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.getUserTask(userDetails)));
    }


}
