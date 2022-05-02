package com.project.blog.exception.Handler;

import com.project.blog.exception.CustomException;
import com.project.blog.exception.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import static com.project.blog.exception.ErrorCode.USED_EMAIL;

@RestControllerAdvice //view를 사용하지 않고, REST API로만 사용할 수 있다.
@Slf4j
public class ErrorExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConstraintViolationException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<ErrorResponse> handleDataException() { //hibernate 관련 에러를 처리한다.
        log.error("handleDataException throw Exception : {}", USED_EMAIL);
        return ErrorResponse.toResponseEntity(USED_EMAIL);
    }

    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) { // 직접 정의한 CustomExeption을 사용한다.
        log.error("handleCustomException throws CustomException : {}", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode()); //exception 발생시 넘겨받은 errorcode를 이용해 사용자에게 보여주는 에러 메세지를 정의한다.
    }
}