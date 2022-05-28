package com.project.blog.controller;

import com.project.blog.domain.Board;
import com.project.blog.dto.Request.BoardCreateDto;
import com.project.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("board/write")
    public Board CreateBoard(
            @Valid @RequestParam MultipartFile file,
            @Valid @RequestPart BoardCreateDto boardCreateDto
            ) throws Exception {
        return boardService.CreateBoard(file, boardCreateDto);
    }

    // 이미지 여러개는 for문 조지면 될려나

    // 게시글 수정

    // 게시글 보기

    // 전체 게시그 보기



}
