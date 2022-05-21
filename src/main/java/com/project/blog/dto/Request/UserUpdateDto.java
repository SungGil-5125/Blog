package com.project.blog.dto.Request;

import com.project.blog.domain.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    private String name;
    private String email;
    private String password;
    private String newPassword;
}
