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

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
//@RequestMapping("Devlog")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    @PostMapping("/user/register")
    public CommonResultResponse SignUp(@RequestBody UserSignupDto userSignupDto) {
        userService.join(userSignupDto);
        return responseService.getSuccessResult();
    }

    @PostMapping("/user/login")
    public UserLoginResponseDto Login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    @GetMapping("/user/logout")
    public void LogOut () {
        userService.Logout();
    }

    @PatchMapping("/user/update")
    public CommonResultResponse UpdateProfile(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "newPassword") String newPassword, //현재 pw와 NewPw가 같을때 처리
            @RequestParam MultipartFile file) throws IOException {

        userService.updateProfile(name, password, newPassword, file);

        return responseService.getSuccessResult();
    }

    @GetMapping("/user_image")
    public ResponseEntity<FileSystemResource> user_profile() throws IOException {
        return userService.getProfile_img();
    }

    @GetMapping("/user_name")
    public UserResponseDto user_profile_name() {
        return userService.getProfile_name();
    }

    @GetMapping("user_image/{user_id}")
    public ResponseEntity<FileSystemResource> BoardProfileImage(@PathVariable("user_id") Long user_id) throws IOException {
        return userService.BoardProfileImage(user_id);
    }
}
