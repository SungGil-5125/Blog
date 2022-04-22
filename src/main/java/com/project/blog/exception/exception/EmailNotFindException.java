package com.project.blog.exception.exception;

import com.project.blog.exception.ErrorCode;

public class EmailNotFindException extends RuntimeException {
    private ErrorCode errorCode;

    public EmailNotFindException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
