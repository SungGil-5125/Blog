package com.project.blog.exception.Handler;

import com.project.blog.exception.ErrorResponse;
import com.project.blog.exception.exception.UsedEmailException;
import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestControllerAdvice
@Slf4j
public class ErrorExceptionHandler {

    @ExceptionHandler(UsedEmailException.class)
    public ResponseEntity<ErrorResponse> UsedEmailExceptionHandler(HttpServletRequest request, HttpServletResponse response, UsedEmailException ex) {

        printExceptionMessage(request, ex, "Email is already used");

        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode());

        System.out.println("/////////errorStatus///////// = " + ex.getErrorCode().getStatus());

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    private void printExceptionMessage(HttpServletRequest request, Exception ex, String message) {
        log.error(request.getRequestURI());
        log.error(message);
        ex.printStackTrace();
    }
}