package com.project.blog.service;

import com.project.blog.domain.Board;
import com.project.blog.domain.Image;
import com.project.blog.domain.User;
import com.project.blog.dto.Request.BoardCreateDto;
import com.project.blog.exception.CustomException;
import com.project.blog.repository.BoardRepository;
import com.project.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageReader;
import java.io.File;
import java.io.IOException;

import static com.project.blog.exception.ErrorCode.IMAGE_NOT_FOUND;
import static com.project.blog.exception.ErrorCode.USER_NOT_FIND;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
//    private final

    @Transactional
    public Board CreateBoard(String user_email, MultipartFile file, String title, String content, String date) throws IOException {

        User user = userRepository.findByEmail(user_email)
                .orElseThrow(()-> new CustomException(USER_NOT_FIND));

        BoardCreateDto boardCreateDto = BoardCreateDto.builder()
                .title(title)
                .content(content)
                .date(date)
                .build();

        Board board = boardCreateDto.toEntity(user);

        updateBoard_image(user_email, file, board);

        return boardRepository.save(board);
    }

    @Transactional
    public void updateBoard_image(String user_email, MultipartFile file, Board board) throws IOException {

        if(file.isEmpty()) {
            throw new CustomException(IMAGE_NOT_FOUND);
        }

        String absolutePath = new File("").getAbsolutePath() + "\\";
        String path = "board_image" +File.separator + user_email + File.separator; //current_date
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

        Image image = Image.builder()
                .board(board)
                .url(String.valueOf(folder))
                .build();



        file.transferTo(folder);
    }
}
