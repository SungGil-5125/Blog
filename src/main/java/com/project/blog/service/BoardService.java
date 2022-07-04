package com.project.blog.service;

import com.project.blog.domain.Board;
import com.project.blog.domain.User;
import com.project.blog.dto.Request.BoardCreateDto;
import com.project.blog.dto.Response.AllBoardResponseDto;
import com.project.blog.dto.Response.BoardListResponseDto;
import com.project.blog.dto.Response.BoardResponseDto;
import com.project.blog.exception.CustomException;
import com.project.blog.repository.BoardRepository;
import com.project.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static com.project.blog.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;
    private final S3Service s3Service;
    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.board_dir}")
    private String dirName;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.board_normal_url}")
    private String normal_image;

    // 블로그 생성
    @Transactional
    public Board CreateBoard(MultipartFile file, String title, String content, String date) throws IOException {

        User user = userService.CurrentUserUtil();

        BoardCreateDto boardCreateDto = BoardCreateDto.builder()
                .title(title)
                .content(content)
                .date(date)
                .build();

        String uploadUrl = s3Service.upload(file, dirName);

        if(file.isEmpty()) {
            uploadUrl = normal_image;
        }

        Board board = boardCreateDto.toEntity(user, uploadUrl);

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
                .user_id(board.getUser().getUser_id())
                .user_name(board.getUser().getName())
                .build();
    }

    // 모든 게시글 보기
    @Transactional
    public List<AllBoardResponseDto> getAllBoards() {

        List<Board> findByAllBoards = boardRepository.findAll();
        List<AllBoardResponseDto> blogs = new ArrayList<>();

        findByAllBoards.forEach(board -> {
            blogs.add(AllBoardResponseDto.builder()
                    .board_id(board.getBoard_id())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .url(board.getUrl())
                    .date(board.getDate())
                    .user_name(board.getUser().getName())
                    .user_id(board.getUser().getUser_id())
                    .build());
            });

        return blogs;

    }

    // 모든 게시글 사진
    @Transactional
    public String getAllBoardsImage(Long board_id) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(()-> new CustomException(BOARD_NOT_FOUND));

        String url = board.getUrl();

        if(url == null) {
            return normal_image;
        }

        return url;
    }

    // 블로그 삭제
    @Transactional
    public void DeleteBlog(Long board_id) {

        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        s3Service.deleteFile(board.getUrl().substring(57));

        boardRepository.delete(board);
    }

    // user_id에 맞는 블로그 전체를 보여 준다
    @Transactional
    public BoardListResponseDto getMyBoards() {

        User user = userService.CurrentUserUtil();
        BoardListResponseDto boardListResponseDto = getBoards(user);

        return boardListResponseDto;
    }

    // 다른 사람 블로그 보여주기
    @Transactional
    public BoardListResponseDto getOthersBoards(Long user_id) {

        User user = userRepository.findById(user_id)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));

        BoardListResponseDto boardListResponseDto = getBoards(user);
        return boardListResponseDto;
    }

    // id에 맞는 블로그 보여 주기
    @Transactional
    public BoardListResponseDto getBoards(User user) {

        List<Board> boards = boardRepository.findByUser_Id(user.getUser_id());


        List<BoardResponseDto> data = new ArrayList<>();

        for(Board board : boards) {

            Long board_id = board.getBoard_id();
            String title = board.getTitle();
            String content = board.getContent();
            String date = board.getDate();

            BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                    .board_id(board_id)
                    .title(title)
                    .content(content)
                    .date(date)
                    .build();
            data.add(boardResponseDto);
        }

        BoardListResponseDto boardListResponseDto = new BoardListResponseDto(data);

        return boardListResponseDto;
    }
}
