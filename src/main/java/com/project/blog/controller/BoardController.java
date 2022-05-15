package com.project.blog.controller;

import com.project.blog.domain.Image;
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
    public List<Image> CreateBoard(
            @RequestPart(value = "file", required = false) List<MultipartFile> file
            //@RequestPart BoardCreateRequestDto boardCreateRequestDto
            ) throws Exception {

        return boardService.CreateBoard(file); //boardCreateRequestDto,
    }

}
