package com.project.blog.controller;

import com.project.blog.domain.Board;
import com.project.blog.dto.Request.BoardCreateRequestDto;
import com.project.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("board/write")
    public Board CreateBoard(
            @RequestPart(value = "image", required = false)List<MultipartFile> files,
            @RequestPart BoardCreateRequestDto boardCreateRequestDto
            ) throws Exception {

        return boardService.CreateBoard(boardCreateRequestDto, files);
    }

}
