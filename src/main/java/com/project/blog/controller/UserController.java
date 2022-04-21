package com.project.blog.controller;

import com.project.blog.domain.User;
import com.project.blog.dto.Request.UserLoginDto;
import com.project.blog.dto.Request.UserSignupDto;
import com.project.blog.dto.Response.TokenResponseDto;
import com.project.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("blog")
public class UserController {

    private final UserService userService;

    @GetMapping("register")
    public String register() {
        return "회원가입 페이지";
    }

    @GetMapping("login")
    public String login() {
        return "로그인 페이지";
    }

    @PostMapping("register")
    public User Signup(@RequestBody UserSignupDto userSignupDto) {
        return userService.join(userSignupDto);
    }

    @PostMapping("login")
    public TokenResponseDto Login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }
}
