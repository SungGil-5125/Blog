package com.project.blog.exception.exception;

import com.project.blog.exception.ErrorCode;

public class EmailNotFind extends RuntimeException {
    private ErrorCode errorCode;

    public EmailNotFind (String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
