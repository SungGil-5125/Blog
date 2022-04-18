package com.project.blog.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private String Roles = "roles";
    private final Long accessTokenVaildMillisecond = 60 * 60 * 1000L; //1 hours
    private final Long refreshTokenValidMillisecond = 14 * 24 * 60 * 60 * 1000L; //14 days

    @
}
