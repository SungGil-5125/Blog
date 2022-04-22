package com.project.blog.exception.exception;

import com.project.blog.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UsedEmailException extends RuntimeException {
    private ErrorCode errorCode;

    public UsedEmailException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
