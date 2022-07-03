package com.project.blog.config.security.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.blog.exception.ErrorCode;
import com.project.blog.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.project.blog.exception.ErrorCode.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException e) {
            log.error("[ ExceptionHandlerFilter ] 에서 ExpiredJwtException 발생");
            setErrorResponse(TOKEN_EXPIRATION, response);
        }catch (JwtException | IllegalArgumentException e) {
            log.error("[ ExceptionHandlerFilter ] 에서 JwtException 발생");
            setErrorResponse(TOKEN_INVALID, response);
        }catch (Exception e) {
            log.error("[ ExceptionHandlerFilter ] 에서 Exception 발생");
            setErrorResponse(UNKNOWN_ERROR, response);
        }
    }

    public void setErrorResponse(ErrorCode errorCode, HttpServletResponse response) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json; charset=utf-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().name()) // 예를 들어 Bad request 같은거?
                .code(errorCode.name())
                .message(errorCode.getMsg())
                .build();

        String errorResponseEntityToJson = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(errorResponseEntityToJson);

    }

}
