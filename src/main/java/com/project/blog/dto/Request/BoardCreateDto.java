package com.project.blog.dto.Request;

import com.project.blog.domain.Board;
import com.project.blog.domain.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
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
                .url("https://devlog-s3-bucket.s3.ap-northeast-2.amazonaws.com/board_image/" + url)
                .build();
    }

}
