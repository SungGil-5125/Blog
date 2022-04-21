package com.project.blog.exception.exception;

import com.project.blog.exception.ErrorCode;

public class RefreshTokenExpriation extends RuntimeException {
    private ErrorCode errorCode;

    public RefreshTokenExpriation(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
