package com.project.blog.dto.Request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    private String name;
    private String password;
    private String newPassword;

}
