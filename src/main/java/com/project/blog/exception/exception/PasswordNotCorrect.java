package com.project.blog.exception.exception;

import com.project.blog.exception.ErrorCode;

public class PasswordNotCorrect extends RuntimeException {
    private ErrorCode errorCode;

    public PasswordNotCorrect (String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
