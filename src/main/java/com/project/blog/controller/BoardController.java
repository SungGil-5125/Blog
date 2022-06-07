package com.project.blog.controller;

import com.project.blog.dto.Response.BoardListResponseDto;
import com.project.blog.dto.Response.BoardResponseDto;
import com.project.blog.response.ResponseService;
import com.project.blog.response.result.CommonResultResponse;
import com.project.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final ResponseService responseService;

    @PostMapping("board/write")
    public CommonResultResponse CreateBoard(
            @RequestParam MultipartFile file,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "date") String date
    ) throws Exception {

        boardService.CreateBoard(file, title, content, date);
        return responseService.getSuccessResult();

    }

    // 게시글 보기
    @GetMapping("/board/{board_id}")
    public BoardResponseDto BlogIn(@PathVariable("board_id") Long board_id) {
        return boardService.BoardIn(board_id);
    }

    // 게시글 삭제
    @DeleteMapping("/Delete/board/{board_id}")
    public CommonResultResponse DeleteBlog(@PathVariable("board_id") Long board_id) {
        boardService.DeleteBlog(board_id);
        return responseService.getSuccessResult();
    }

    // 전체 게시글 불러오기
    @GetMapping("board")
    public BoardListResponseDto showAllBlog() {
        return boardService.getAllBoards();
    }

    // 이미지 여러개는 for문 조지면 될려나

}
