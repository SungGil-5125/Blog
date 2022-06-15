package com.project.blog.dto.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class UserResponseDto {

    private Long user_id;
    private String url;
    private int board_number;
    private String email;
    private String name;

}
