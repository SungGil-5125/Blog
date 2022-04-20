package com.project.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Column;

@Controller
public class TestController {
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
