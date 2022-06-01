package com.project.blog.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@EnableWebSecurity(debug = true) //request가 올때마다 어떤 filter를 사용하는지 출력
public class JwtRequestFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserDetailsService userService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader("Authorization"); // 헤더 요청해서 값 얻기
        String refreshToken = request.getHeader("RefreshToken");

        if(accessToken != null && refreshToken != null && tokenProvider.getTokenType(accessToken).equals("accessToken")){
            if(tokenProvider.isTokenExpired(accessToken) && tokenProvider.getTokenType(accessToken).equals("refreshToken") && !tokenProvider.isTokenExpired(refreshToken)) {
                accessToken = generateNewAccessToken(refreshToken);
                writeResponse(response, accessToken); //헤더 추출 accessToken
            }
            String userEmail = accessTokenExractEmail(accessToken);

            if(userEmail != null) {
                registerUserInfoInSecurityContext(userEmail, request);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void writeResponse(HttpServletResponse response, String accessToken) throws IOException {
        String bodyToJson = getBodyToJson();
        response.addHeader("accessToken", accessToken);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(bodyToJson);
    }

    private String getBodyToJson() throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        body.put("success" , true);
        body.put("msg", "token is regenerated");
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        String bodyToJson = objectMapper.writeValueAsString(body);
        return bodyToJson;
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
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        }catch (NullPointerException e) {
            throw new RuntimeException();
        }
    }

    // 토큰 생성 해주는 메서드
    private String generateNewAccessToken(String refreshToken) {
        try {
            return tokenProvider.generateAccessToken(tokenProvider.getUserEmail(refreshToken));
        }catch (JwtException | IllegalStateException | MalformedInputException | SignatureException e) {
            throw new RuntimeException();
        }
    }
}
