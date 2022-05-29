package com.project.blog.service;

import com.project.blog.domain.Board;
import com.project.blog.domain.User;
import com.project.blog.dto.Request.BoardCreateDto;
import com.project.blog.exception.CustomException;
import com.project.blog.repository.BoardRepository;
import com.project.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.project.blog.exception.ErrorCode.IMAGE_NOT_FOUND;
import static com.project.blog.exception.ErrorCode.USER_NOT_FIND;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public Board CreateBoard(MultipartFile file, BoardCreateDto boardCreateDto) throws Exception {

        Board board = boardCreateDto.toEntity();
        board.userMapping();

        updateBoard_image(file);

        return boardRepository.save(board);

        //jwt를 쓰면 header에 저장되는데 이게 postman header?
        //

    }

    @Transactional
    public void updateBoard_image(MultipartFile file) throws IOException {

        if(file.isEmpty()) {
            throw new CustomException(IMAGE_NOT_FOUND);
        }

        //사진 업로드
        String absolutePath = new File("").getAbsolutePath() + "\\";
        String path = "profile" + File.separator; //current_date
        File folder = new File(path);

        if(!folder.exists()) {
            folder.mkdirs();
        }

        String originalFileExtension = null;
        String contentType = file.getContentType();

        if(contentType.contains("image/jpeg") || contentType.contains("image/png")) {
            originalFileExtension = ".jpg";
        }

        //String new_file_name = user_id + originalFileExtension;

        //folder = new File(absolutePath + path + File.separator + new_file_name);
        file.transferTo(folder);


    }




}
