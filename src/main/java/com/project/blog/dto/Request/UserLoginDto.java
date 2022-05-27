package com.project.blog.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginDto {

    @Email
    private String email;
    private String password;

}
