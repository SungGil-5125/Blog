package com.project.blog.dto.Request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.project.blog.domain.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor //전체 인자를 갖는 생성자를 만들어줌
public class UserSignupDto {

    @NotBlank(message = "이메일이 입력되지 않았습니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank(message = "패스워드가 입력되지 않았습니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이여야 합니다.")
    private String password;

    public User toEntity(){
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
