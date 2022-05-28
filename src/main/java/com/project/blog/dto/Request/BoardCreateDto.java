package com.project.blog.dto.Request;

import com.project.blog.domain.Board;
import com.project.blog.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardCreateDto {

    private Long board_id;
    private User user_id;
    private String title;
    private String board_image;
    private String content;
    private String date;

    public Board toEntity() {
        return Board.builder()
                .user(user_id)
                .title(title)
                .content(content)
                .date(date)
                .build();
    }
}
