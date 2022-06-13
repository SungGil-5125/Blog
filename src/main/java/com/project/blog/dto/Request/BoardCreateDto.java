package com.project.blog.dto.Request;

import com.project.blog.domain.Board;
import com.project.blog.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BoardCreateDto {

    private Long board_id;
    private String title;
    private String content;
    private String date;

    public Board toEntity(User user, String url) {
        return Board.builder()
                .title(title)
                .content(content)
                .date(date)
                .user(user)
                .url(url)
                .build();
    }

}
