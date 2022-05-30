package com.project.blog.controller;

import com.project.blog.domain.Board;
import com.project.blog.domain.User;
import com.project.blog.dto.Request.BoardCreateDto;
import com.project.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("{user_email}/board/write")
    public Board CreateBoard(
            @PathVariable("user_email") String user_email,
            @RequestParam MultipartFile file,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "date") String date
    ) throws Exception {

        return boardService.CreateBoard(user_email, file, title, content, date);

    }

    // 이미지 여러개는 for문 조지면 될려나

    // 게시글 수정

    // 게시글 보기

    // 전체 게시그 보기



}
