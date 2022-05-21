package com.project.blog.dto.Response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private Long User_id;
    private String User_email;
    private String User_name;

}
