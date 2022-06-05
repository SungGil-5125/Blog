package com.project.blog.service;

import com.project.blog.domain.Board;
import com.project.blog.domain.User;
import com.project.blog.dto.Request.BoardCreateDto;
import com.project.blog.dto.Response.BoardResponseDto;
import com.project.blog.exception.CustomException;
import com.project.blog.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.project.blog.exception.ErrorCode.BOARD_NOT_FOUND;
import static com.project.blog.exception.ErrorCode.IMAGE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;

    @Transactional
    public Board CreateBoard(MultipartFile file, String title, String content, String date) throws IOException {

        User user = userService.CurrentUserUtil();

        BoardCreateDto boardCreateDto = BoardCreateDto.builder()
                .title(title)
                .content(content)
                .date(date)
                .build();

        Board board = boardCreateDto.toEntity(user);

        updateBoard_image(file, board, user);

        return boardRepository.save(board);
    }

    @Transactional
    public BoardResponseDto BoardIn(Long board_id) {

        Board board = boardRepository.findById(board_id)
                .orElseThrow(()-> new CustomException(BOARD_NOT_FOUND));

        return BoardResponseDto.builder()
                .board_id(board.getBoard_id())
                .title(board.getTitle())
                .content(board.getContent())
                .date(board.getDate())
                .user(board.getUser())
                .build();
    }

    @Transactional
    public void DeleteBlog(Long board_id) {

        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        boardRepository.delete(board);
    }

    @Transactional
    public void updateBoard_image(MultipartFile file, Board board, User user) throws IOException {

        if(file.isEmpty()) {
            throw new CustomException(IMAGE_NOT_FOUND);
        }

        String absolutePath = new File("").getAbsolutePath() + "\\";
        String path = "board_image" +File.separator + user.getEmail() + File.separator; //current_date
        File folder = new File(path);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        String originalFileExtension = null;
        String contentType = file.getContentType();

        if (contentType.contains("image/jpeg") || contentType.contains("image/png")) {
            originalFileExtension = ".jpg";
        }

        String file_name = file + originalFileExtension;

        folder = new File(absolutePath + path + File.separator + file_name);

//        Image image = Image.builder()
//                .board(board)
//                .url(String.valueOf(folder))
//                .build();
//

        file.transferTo(folder);
    }
}
