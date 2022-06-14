package com.project.blog.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AllBoardListResponseDto {

    List<AllBoardResponseDto> blogs;
}
