package com.project.blog.controller;

import com.project.blog.dto.Response.AllBoardListResponseDto;
import com.project.blog.dto.Response.AllBoardResponseDto;
import com.project.blog.dto.Response.BoardListResponseDto;
import com.project.blog.dto.Response.BoardResponseDto;
import com.project.blog.response.ResponseService;
import com.project.blog.response.result.CommonResultResponse;
import com.project.blog.service.BoardService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final ResponseService responseService;

    // 게시글 생성
    @PostMapping("board/write")
    public CommonResultResponse CreateBoard(
            @RequestPart(value = "file", required = false) MultipartFile multipartFile,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "date") String date
            ) throws Exception {

        boardService.CreateBoard(multipartFile, title, content, date);
        return responseService.getSuccessResult();

    }

    // 게시글 보기
    @GetMapping("/board/{board_id}")
    public BoardResponseDto BlogIn(@PathVariable("board_id") Long board_id) {
        return boardService.BoardIn(board_id);
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{board_id}")
    public CommonResultResponse DeleteBlog(@PathVariable("board_id") Long board_id) {
        boardService.DeleteBlog(board_id);
        return responseService.getSuccessResult();
    }

    // 전체 게시글 불러 오기
    @GetMapping("board")
    public AllBoardListResponseDto showAllBlog() {
        return boardService.getAllBoards();
    }

    // 게시글 이미지 보여 주기
    @GetMapping("board_image/{board_id}")
    public String showAllBlogImage(@PathVariable("board_id") Long board_id) {
        return boardService.getAllBoardsImage(board_id);
    }

    // 나의 게시글들 보기 <- 이거는 보류...
    @GetMapping("board/myBoard")
    public BoardListResponseDto getMyBoards() {
       return boardService.getMyBoards();
    }

    // 다른 사람 게시글들 보기
    @GetMapping("/boards/{user_id}")
    public BoardListResponseDto getOtherBoards(@PathVariable("user_id") Long user_id) {
        return boardService.getOthersBoards(user_id);
    }
}
