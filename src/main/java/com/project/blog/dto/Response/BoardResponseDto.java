package com.project.blog.dto.Response;

import com.project.blog.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BoardResponseDto {

    private Long board_id;
    private String user_name;
    private String title;
    private String content;
    private String date;

}
