package com.project.blog.controller;

import com.project.blog.domain.User;
import com.project.blog.dto.Request.UserLoginDto;
import com.project.blog.dto.Request.UserSignupDto;
import com.project.blog.dto.Response.TokenResponseDto;
import com.project.blog.dto.Response.UserResponseDto;
import com.project.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;


@RestController
@RequiredArgsConstructor
//@RequestMapping("Devlog")
public class UserController {

    private final UserService userService;

    @GetMapping("/user/register")
    public String register() {
        return "회원가입 페이지";
    }

    @GetMapping("/user/login")
    public String login() {
        return "로그인 페이지";
    }

//    @GetMapping("/user/update/{user_id}")
//    public UserResponseDto UpdateUserProfile(@PathVariable("user_id") Long user_id){
//        return userService.UpdateProfile(user_id);
//    }

    @GetMapping("/user/{user_id}")
    @ResponseBody
    public UserResponseDto user_profile(@PathVariable("user_id") Long user_id) {
        return userService.profile(user_id);
    }

    @PostMapping("/user/register")
    public User Signup(@RequestBody UserSignupDto userSignupDto) {
        return userService.join(userSignupDto);
    }

    @PostMapping("/user/login")
    public TokenResponseDto Login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    @PostMapping("/user/upload")
    public Boolean fileUpload(@RequestParam("file")MultipartFile file) throws Exception {
        return userService.uploadImage(file);
    }
}
