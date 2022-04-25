package com.project.blog.dto.Request;

import com.project.blog.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardFormRequestDto {

    private Long board_id;
    private String title;
    private String desc;
    private String date;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .desc(desc)
                .date(date)
                .build();
    }
}
