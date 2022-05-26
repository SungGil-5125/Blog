package com.project.blog.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
public class JwtRequestFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserDetailsService userService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("Authorization");
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
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }catch (NullPointerException e) {
            throw new RuntimeException();
        }
    }

    private String generateNewAccessToken(String refreshToken) {
        try {
            return tokenProvider.generateAccessToken(tokenProvider.getUserEmail(refreshToken));
        }catch (JwtException | IllegalStateException | MalformedInputException | SignatureException e) {
            throw new RuntimeException();
        }
    }
}
