package com.project.blog.repository;

import com.project.blog.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

   List<Board> findAll();

   @Query(nativeQuery = true, value = "select b.board_id, b.content, b.title, b.date, b.url, b.user_id from board b where b.user_id=:user_id")
   List<Board> findByUser_Id(Long user_id);
}
