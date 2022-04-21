package com.project.blog.controller;

import com.project.blog.domain.User;
import com.project.blog.dto.Request.UserLoginDto;
import com.project.blog.dto.Request.UserSignupDto;
import com.project.blog.dto.Response.TokenDto;
import com.project.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("blog")
public class UserController {

    private final UserService userService;

    @PostMapping("user/join")
    public User Signup(@RequestBody UserSignupDto userSignupDto) {
        return userService.join(userSignupDto);
    }

    @PostMapping("user/login")
    public TokenDto Login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }
}
