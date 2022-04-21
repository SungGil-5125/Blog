package com.project.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    PASSWORD_NOT_CORRECT(404, "Password is not correct"),
    USED_EMAIL(400, "Email is already used"),
    EMAIL_NOT_FIND(404, "Email is not find"),
    REFRESH_TOKEN_EXPIRATION(401, "Refresh token is Expiration")
    ;

    private int status;
    private String msg;
}
