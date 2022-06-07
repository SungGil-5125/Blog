package com.project.blog.repository;

import com.project.blog.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

   List<Board> findAllByBoard();

}
