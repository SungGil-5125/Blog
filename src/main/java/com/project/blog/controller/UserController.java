package com.project.blog.controller;

import com.project.blog.domain.User;
import com.project.blog.dto.Request.UserLoginDto;
import com.project.blog.dto.Request.UserSignupDto;
import com.project.blog.dto.Request.UserUpdateDto;
import com.project.blog.dto.Response.TokenResponseDto;
import com.project.blog.dto.Response.UserResponseDto;
import com.project.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.management.MemoryUsage;
import java.lang.reflect.Parameter;
import java.util.List;

@RestController
@RequiredArgsConstructor
//@RequestMapping("Devlog")
public class UserController {

    private final UserService userService;

    @GetMapping("/user/login")
    public String login() {
        return "로그인 페이지";
    }

    @GetMapping("{user_id}")
    public UserResponseDto user_profile(@PathVariable("user_id") Long user_id) {
        return userService.profile(user_id);
    }

    @PostMapping("/user/register")
    public User SignUp(@RequestBody UserSignupDto userSignupDto) {
        return userService.join(userSignupDto);
    }

    @PostMapping("/user/login")
    public TokenResponseDto Login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    @PutMapping("{user_id}/update")
    public void UpdateProfile(
            @PathVariable("user_id") Long user_id,
            @RequestBody UserUpdateDto userUpdateDto,
            @RequestParam MultipartFile file) throws IOException {
        userService.updateProfile(user_id, userUpdateDto, file);
    }
}
