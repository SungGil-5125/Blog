package com.project.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청*/
    USED_EMAIL(BAD_REQUEST, "이미 사용하는 이메일입니다."),
    SAME_PASSWORD(BAD_REQUEST, "이미 사용 중인 비밀번호입니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    REFRESH_TOKEN_EXPIRATION(UNAUTHORIZED, "Refresh Token이 만료되었습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다"),
    PASSWORD_NOT_CORRECT(NOT_FOUND, "비밀번호가 맞지 않습니다."),
    IMAGE_NOT_FOUND(NOT_FOUND, "이미지가 선택되지 않았습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    USER_NOT_FIND(CONFLICT, "계정을 찾을 수 없습니다."),
    ;

    private HttpStatus httpStatus;
    private String msg;
}
