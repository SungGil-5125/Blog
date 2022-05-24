package com.project.blog.dto.Response;

import com.project.blog.domain.Board;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@Getter
@Builder
public class UserResponseDto {

    private Long user_id;
    private String email;
    private String name;
    private String prifile_image;
    private List<Board> boards;

}
