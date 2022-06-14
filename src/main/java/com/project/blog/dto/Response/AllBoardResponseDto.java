package com.project.blog.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AllBoardResponseDto {

    private Long board_id;
    private Long user_id;
    private String title;
    private String content;
    private String date;
    private String url;
    private String user_name;
}
