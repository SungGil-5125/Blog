package com.project.blog.dto.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserLoginResponseDto {

    private Long user_id;
    private String AccessToken;
    private String RefreshToken;
}
