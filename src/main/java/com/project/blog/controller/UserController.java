package com.project.blog.controller;

import com.project.blog.dto.User.UserLoginDto;
import com.project.blog.dto.User.UserSignupDto;
import com.project.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("blog")
public class UserController {

    private final UserService userService;

    public UserSignupDto Signup(UserSignupDto userSignupDto) {
        userService.join(userSignupDto);
        return userSignupDto;
    }

    public UserSignupDto Login(UserLoginDto userLoginDto) {

    }
}
