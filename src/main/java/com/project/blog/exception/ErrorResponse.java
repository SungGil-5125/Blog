package com.project.blog.exception;

public class ErrorResponse {

    private String msg;

    public ErrorResponse(ErrorCode errorCode) {
        this.msg = errorCode.getMsg();
    }
}
