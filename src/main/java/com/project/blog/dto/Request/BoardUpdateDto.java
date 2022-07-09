package com.project.blog.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoardUpdateDto {

    private String title;
    private String content;
    private String date;

}
