package com.project.blog.controller;

import com.project.blog.domain.User;
import com.project.blog.dto.Request.UserLoginDto;
import com.project.blog.dto.Request.UserSignupDto;
import com.project.blog.dto.Response.UserLoginResponseDto;
import com.project.blog.dto.Response.UserResponseDto;
import com.project.blog.response.ResponseService;
import com.project.blog.response.result.CommonResultResponse;
import com.project.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
//@RequestMapping("Devlog")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    // 회원 가입
    @PostMapping("/user/register")
    public CommonResultResponse SignUp(@RequestBody UserSignupDto userSignupDto) {
        userService.join(userSignupDto);
        return responseService.getSuccessResult();
    }

    // 로그인
    @PostMapping("/user/login")
    public UserLoginResponseDto Login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    // 로그 아웃
    @GetMapping("/user/logout")
    public void LogOut () {
        userService.Logout();
    }

    // 프로필 수정
    @PatchMapping("/user/update")
    public CommonResultResponse UpdateProfile(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "newPassword") String newPassword, //현재 pw와 NewPw가 같을때 처리
            @RequestParam MultipartFile file) throws IOException {

        userService.updateProfile(name, password, newPassword, file);

        return responseService.getSuccessResult();
    }

    // 프로필 정보
    @GetMapping("/user_profile/{user_id}")
    public UserResponseDto user_profile(@PathVariable Long user_id) {
        return userService.getMyProfileImage(user_id);
    }

    // 인증 객체 리턴
    @GetMapping("user_name")
    public User user_detail() {
        return userService.CurrentUserUtil();
    }

    // Token으로 프로필 이미지 보기
    @GetMapping("/user_image")
    public String user_image() {
        return userService.getProfileImage();
    }

    // user_id로 프로필 이미지 보기
    @GetMapping("user_image/{user_id}")
    public String BoardProfileImage(@PathVariable("user_id") Long user_id) {
        return userService.getProfileImage(user_id);
    }
}
