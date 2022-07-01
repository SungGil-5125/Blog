package com.project.blog.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.MalformedInputException;
import java.security.SignatureException;

@Component
@RequiredArgsConstructor
//@EnableWebSecurity(debug = true) //request가 올때마다 어떤 filter를 사용하는지 출력
public class JwtRequestFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserDetailsService userService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        String accessToken = request.getHeader("Authorization"); // 헤더 요청해서 값 얻기

        if (tokenProvider.isTokenExpired(accessToken) && tokenProvider.getTokenType(accessToken).equals("accessToken") && !tokenProvider.isTokenExpired(accessToken)) {
            String userEmail = accessTokenExractEmail(accessToken);
            if (userEmail != null) registerUserInfoInSecurityContext(userEmail, request);
        }

        filterChain.doFilter(request,response);
}

    private String accessTokenExractEmail(String accessToken) {
        try {
            return tokenProvider.getUserEmail(accessToken);
        } catch (JwtException | IllegalArgumentException | MalformedInputException | SignatureException e) {
            throw new RuntimeException();
        }
    }

    private void registerUserInfoInSecurityContext(String userEmail, HttpServletRequest req) {
        try{
            UserDetails userDetails = userService.loadUserByUsername(userEmail);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // UsernamePasswordAuthenticationToken은 추후 인증이 끝나고 SecurityContextHolder.getContext()에 등록될 Authentication 객체 이다.
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req)); // 현재 request 기반으로 등록하기 위해서
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        }catch (NullPointerException e) {
            throw new RuntimeException();
        }
    }
}
