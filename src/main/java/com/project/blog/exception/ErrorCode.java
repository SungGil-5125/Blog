package com.project.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    USED_EMAIL(BAD_REQUEST, "이미 사용하는 이메일입니다."),
    WRONG_IMAGE_EXTENSION(BAD_REQUEST, "잘못된 이미지 확장자명입니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    TOKEN_EXPIRATION(UNAUTHORIZED, "만료된 Token입니다."),

    /* 403 FORBIDDEN : 웹 페이지를 볼 권한이 없음 */
    TOKEN_INVALID(FORBIDDEN, "유효하지 않은 Token입니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다"),
    PASSWORD_NOT_CORRECT(NOT_FOUND, "비밀번호가 맞지 않습니다."),
    IMAGE_NOT_FOUND(NOT_FOUND, "이미지가 선택되지 않았습니다."),
    BOARD_NOT_FOUND(NOT_FOUND, "블로그가 없습니다."),
    HIBERNATE_ERROR(NOT_FOUND, "hibernate Exception"),
    TOKEN_REFRESH_FAIL(NOT_FOUND, "토큰 재발급에 실패 했습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    USER_NOT_FOUND(CONFLICT, "계정을 찾을 수 없습니다."),

    /* 500 INTERNAL SERVER ERROR */
    UNKNOWN_ERROR(INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러입니다.")
    ;

    private HttpStatus httpStatus;
    private String msg;
}
