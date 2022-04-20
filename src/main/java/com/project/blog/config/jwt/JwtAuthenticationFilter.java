package com.project.blog.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.http.HttpRequest;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final TokenProvider tokenProvider;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = tokenProvider.resolveJwtToken((HttpServletRequest) request); //tokenProvider를 주입받아 헤더에서 토큰을 추출

        if(token != null && tokenProvider.validateTokenExpiration(token)) { //토큰이 존재하는지 확인하고, 존재한다면 만료기간이 지나지 않았는지 롹인한다.
            Authentication auth = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            //성공했다면, 인증객체를 가져오고 SecurityContextHolder에 저장하여 인증을 할 수 있도록 한다.
        }

        chain.doFilter(request, response); //doFilter 메서드를 통해 다음 필터로 넘어가 실제 AuthenticationFilter에서 이미 인증되어 있는 객체를 통해 인증이 되게 한다.
    }
}
