package com.project.blog.controller;

import com.project.blog.domain.Board;
import com.project.blog.dto.Request.BoardCreateRequestDto;
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
            @Valid @RequestParam List<MultipartFile> files,
            @Valid @RequestPart BoardCreateRequestDto boardCreateRequestDto
            ) throws Exception {

        return boardService.CreateBoard(files, boardCreateRequestDto); //boardCreateRequestDto,
    }

}
