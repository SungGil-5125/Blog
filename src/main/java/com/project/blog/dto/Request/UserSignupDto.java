package com.project.blog.dto.Request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.project.blog.domain.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor //전체 인자를 갖는 생성자를 만들어줌
public class UserSignupDto {

    @NotBlank
    //@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$")
    @Email
    private String email;

    @NotBlank(message = "사용자 이름이 입력되지 않았습니다.")
    private String name;

    @NotBlank(message = "패스워드가 입력되지 않았습니다.")
    @Size(min = 4, message = "비밀번호는 4자 이상이여야 합니다.")
    private String password;

    public User toEntity(){
        return User.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();
    }

}
