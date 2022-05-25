package com.project.blog.controller;

import com.project.blog.domain.User;
import com.project.blog.dto.Request.UserLoginDto;
import com.project.blog.dto.Request.UserSignupDto;
import com.project.blog.dto.Response.TokenResponseDto;
import com.project.blog.dto.Response.UserResponseDto;
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

    @GetMapping("/user/login")
    public String login() {
        return "로그인 페이지";
    }

    @GetMapping("{user_name}/user_image")
    public ResponseEntity<FileSystemResource> user_profile(@PathVariable("user_name") Long user_id) throws IOException {
        return userService.getProfile_img(user_id);
    }

    @GetMapping("{user_id}/user_name")
    public UserResponseDto user_profile_name(@PathVariable("user_id") Long user_id) {
        return userService.getProfile_name(user_id);
    }

    @PostMapping("/user/register")
    public User SignUp(@RequestBody UserSignupDto userSignupDto) {
        return userService.join(userSignupDto);
    }

    @PostMapping("/user/login")
    public TokenResponseDto Login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    @PatchMapping("{user_id}/update")
    public void UpdateProfile(
            @PathVariable("user_id") Long user_id,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "newPassword") String newPassword,
            @RequestParam MultipartFile file) throws IOException {
        userService.updateProfile(user_id, name, password, newPassword, file);
    }
}
