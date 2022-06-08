package com.project.blog.service;

import com.project.blog.domain.Board;
import com.project.blog.domain.User;
import com.project.blog.dto.Request.BoardCreateDto;
import com.project.blog.dto.Response.BoardListResponseDto;
import com.project.blog.dto.Response.BoardResponseDto;
import com.project.blog.exception.CustomException;
import com.project.blog.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.project.blog.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;

    // 블로그 생성
    @Transactional
    public Board CreateBoard(MultipartFile file, String title, String content, String date) throws IOException {

        User user = userService.CurrentUserUtil();

        BoardCreateDto boardCreateDto = BoardCreateDto.builder()
                .title(title)
                .content(content)
                .date(date)
                .build();

        Board board = boardCreateDto.toEntity(user);

        System.out.println("board_id = " + board.getBoard_id());
        System.out.println("board.getTitle() = " + board.getTitle());

        updateBoard_image(file, board, user);

        return boardRepository.save(board);
    }

    // 블로그 상세 보기
    @Transactional
    public BoardResponseDto BoardIn(Long board_id) {

        Board board = boardRepository.findById(board_id)
                .orElseThrow(()-> new CustomException(BOARD_NOT_FOUND));

        return BoardResponseDto.builder()
                .board_id(board.getBoard_id())
                .title(board.getTitle())
                .content(board.getContent())
                .date(board.getDate())
                .user_name(board.getUser().getUsername())
                .build();
    }

    // 모든 게시글 보기
    @Transactional
    public BoardListResponseDto getAllBoards() {

        List<Board> findByAllBoards = boardRepository.findAll();
        List<BoardResponseDto> blogs = new ArrayList<>();

        for(int i = 0; i < findByAllBoards.size(); i++){
            Board board = findByAllBoards.get(i);

            Long board_id = board.getBoard_id();
            User user = board.getUser();
            String title = board.getTitle();
            String content = board.getContent();
            String date = board.getDate();

            BoardResponseDto boardResponseDto = new BoardResponseDto(board_id, user.getName(), title, content, date);
            blogs.add(boardResponseDto);
        }

        BoardListResponseDto boardListResponseDto = new BoardListResponseDto(blogs);

        return boardListResponseDto;
    }

    // 모든 게시글 사진
    @Transactional
    public ResponseEntity<FileSystemResource> getAllBoardsImage(Long board_id) throws IOException {

        User user = userService.CurrentUserUtil();

        Board board = boardRepository.findById(board_id)
                        .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        String originUrl = board.getOriginFileName();

        Path path = new File("board_image/" + user.getEmail() + "/" + originUrl).toPath();
        FileSystemResource resource = new FileSystemResource(path);

        if(!resource.exists()) {
            throw new CustomException(IMAGE_NOT_FOUND);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
                .body(resource);

    }

    // 블로그 삭제
    @Transactional
    public void DeleteBlog(Long board_id) {

        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        boardRepository.delete(board);
    }

    // 블로그 사진 업로드 (image entity 수정 해야함)
    @Transactional
    public void updateBoard_image(MultipartFile file, Board board, User user) throws IOException {

        String absolutePath = new File("").getAbsolutePath() + "\\";
        String path = "board_image" + File.separator + user.getEmail() + File.separator; //current_date
        File folder = new File(path);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        String contentType = file.getContentType();

        if(contentType.contains("image/jpg") || contentType.contains("image/png") || contentType.contains("image/gif")) {

            String originalFilename = file.getOriginalFilename();

            board.updateImage(originalFilename);

            folder = new File(absolutePath + path + File.separator + originalFilename);

            file.transferTo(folder);

        } else {
            throw new CustomException(WRONG_IMAGE_EXTENSION);
        }

        /*
        원래 파일이름이랑 user_id, board_id 저장하고
        해당 값에 맞게 이미지 가져오는걸로
         */

    }
}
