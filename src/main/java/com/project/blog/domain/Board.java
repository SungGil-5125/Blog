package com.project.blog.domain;

import com.project.blog.dto.Request.BoardCreateDto;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "board")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long board_id;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = User.class) // merge란 엔티티 상태를 병합할 때 연관된 엔티티도 모두 병합하는것을 의미함.
    @OnDelete(action = OnDeleteAction.CASCADE) //user가 삭제 된다면 게시글도 삭제
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String date;

    public Board(BoardCreateDto boardCreateDto, User user) {
        this.user = user;
        this.title = boardCreateDto.getTitle();
        this.content = boardCreateDto.getContent();
        this.date = boardCreateDto.getDate();
    }
}
