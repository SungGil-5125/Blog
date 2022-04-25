package com.project.blog.service;

import com.project.blog.domain.Board;
import com.project.blog.dto.Request.BoardFormRequestDto;
import com.project.blog.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board CreateBoard(BoardFormRequestDto boardFormRequestDto) {

        Board board = boardFormRequestDto.toEntity();

        return boardRepository.save(board);

    }
}
