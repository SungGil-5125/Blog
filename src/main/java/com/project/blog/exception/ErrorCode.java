package com.project.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    PASSWORD_NOT_CORRECT(404, "Password is not correct"),
    USED_EMAIL(400, "Email is aready used"),
    EMAIL_NOT_FIND(404, "Email is not find")
    ;

    private int status;
    private String msg;
}
