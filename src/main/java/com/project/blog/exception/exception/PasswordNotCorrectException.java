package com.project.blog.exception.exception;

import com.project.blog.exception.ErrorCode;

public class PasswordNotCorrectException extends RuntimeException {
    private ErrorCode errorCode;

    public PasswordNotCorrectException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
