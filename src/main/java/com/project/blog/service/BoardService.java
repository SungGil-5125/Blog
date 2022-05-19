package com.project.blog.service;

import com.project.blog.domain.Board;
import com.project.blog.domain.Image;
import com.project.blog.dto.Request.BoardCreateRequestDto;
import com.project.blog.repository.BoardRepository;
import com.project.blog.repository.ImageRepository;
import com.project.blog.service.Handler.FIleHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final FIleHandler fIleHandler;

    @Transactional
    public Board CreateBoard(List<MultipartFile> files, BoardCreateRequestDto boardCreateRequestDto) throws Exception {

        List<Image> imageList = fIleHandler.parseFileInfo(files);

        if(!imageList.isEmpty()){
            imageRepository.saveAll(imageList);
        }

        Board board = boardCreateRequestDto.toEntity();

        return board;
    }


}
