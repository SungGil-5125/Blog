package com.project.blog.exception.exception;

import com.project.blog.exception.ErrorCode;

public class RefreshTokenExpriationException extends RuntimeException {
    private ErrorCode errorCode;

    public RefreshTokenExpriationException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
