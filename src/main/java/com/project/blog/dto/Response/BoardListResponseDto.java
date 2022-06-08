package com.project.blog.dto.Response;

import com.project.blog.domain.Board;
import com.project.blog.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class BoardListResponseDto {

    List<BoardResponseDto> blogs;

}
