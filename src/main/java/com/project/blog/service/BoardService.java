package com.project.blog.service;

import com.project.blog.domain.Image;
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
    public List<Image> CreateBoard(List<MultipartFile> file) throws Exception{

        //Board board = boardCreateRequestDto.toEntity();

        List<Image> imageList = fIleHandler.parseFileInfo(file);

//        if(!imageList.isEmpty()){
//            imageRepository.saveAll(imageList);
//        }

        return imageRepository.saveAll(imageList);//boardRepository.save(board);
    }


}
