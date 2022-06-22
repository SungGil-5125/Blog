package com.project.blog.config.security.jwt;

import com.project.blog.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SignatureException;
import java.util.Date;

import static com.project.blog.exception.ErrorCode.TOKEN_EXPIRATION;

@Component
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    public static final long ACCESS_TOKEN_EXPIRED_TIME = 1000 * 60 * 60 * 3; //accessToken 만료시간 3시간
    public static long REFRESH_TOKEN_EXPIRED_TIME = 14 * 24 * 60 * 60 * 1000L; //refreshToken 만료 시간 2주

    @AllArgsConstructor
    enum TokenType {
        ACCESS_TOKEN("accessToken"),
        REFRESH_TOKEN("refreshToken")
        ;
        private String value;
    }

    @AllArgsConstructor
    enum TokenClaimName {
        USER_EMAIL("userEmail"),
        TOKEN_TYPE("tokenType")
        ;
        String value;
    }

    private Key getSigningKey(String secretKey){
        byte keyByte[]=secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyByte);
    }

    // 내용 추출 메서드
    public Claims extractAllClaims(String token) throws ExpiredJwtException, IllegalStateException, UnsupportedOperationException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserEmail(String token) throws MalformedInputException, SignatureException {
        if(isTokenExpired(token)) {
            throw new CustomException(TOKEN_EXPIRATION);
        }

        return extractAllClaims(token).get(TokenClaimName.USER_EMAIL.value, String.class);
    }

    public String getTokenType(String token) {
        return extractAllClaims(token).get(TokenClaimName.TOKEN_TYPE.value, String.class);
    }

    public Boolean isTokenExpired(String token) {
        try {
            extractAllClaims(token).getExpiration();
            return false;
        }catch (Exception e){
            return true;
        }
    }

    private String doGenerateToken(String userEmail, TokenType tokenType, Long expireTime) {
        final Claims claims= Jwts.claims();
        claims.put("userEmail", userEmail);
        claims.put("tokenType", tokenType.value);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(String email) {
        return doGenerateToken(email, TokenType.ACCESS_TOKEN, ACCESS_TOKEN_EXPIRED_TIME);
    }

    public String generateRefreshToken(String email) {
        return doGenerateToken(email, TokenType.REFRESH_TOKEN, REFRESH_TOKEN_EXPIRED_TIME);
    }

    public String getRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        if(refreshToken != null && refreshToken.startsWith("Bearer ")) {
            return refreshToken.substring(7);
        }else {
            return null;
        }
    }
}
