package com.project.blog.dto.Response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private Boolean Success;
    private Long User_id;
    private String User_email;
    private String User_name;
    private String User_password;
}
