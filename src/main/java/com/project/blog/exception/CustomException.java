package com.project.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor //모든 필드의 값을 가지는 생성자를 만들어주자나 그러면
@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;


    /*

    private Errorcode(errorcode) {

        this.errorcode = errorcode; 이런 느낌?

    }

     */

}
