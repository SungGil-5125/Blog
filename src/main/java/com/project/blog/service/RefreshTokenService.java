package com.project.blog.service;

import com.project.blog.config.security.jwt.TokenProvider;
import com.project.blog.domain.User;
import com.project.blog.dto.Request.RefreshTokenDto;
import com.project.blog.exception.CustomException;
import com.project.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.project.blog.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public Map<String, String> refreshToken(String refreshToken, RefreshTokenDto refreshTokenDto) {

        String email = refreshTokenDto.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));

        if(user.getRefreshToken() == null) {
            throw new CustomException(TOKEN_INVALID);
        }

        Map<String, String> map = new HashMap<>();
        String newAccessToken = null;
        String newRefreshToken = null;

        if(user.getRefreshToken().equals(refreshToken) && tokenProvider.isTokenExpired(refreshToken)){
            newAccessToken = tokenProvider.generateAccessToken(email);
            newRefreshToken = tokenProvider.generateRefreshToken(email);

            user.updateRefreshToken(newRefreshToken);
            map.put("email", email);
            map.put("NewAccessToken", "Bearer " + newAccessToken);
            map.put("NewRefreshToken", "Bearer " + newRefreshToken);
            // Bearer : 토큰 포맷 형식 사람들이 JWT 토큰인걸 쉽게 알게 하기 위한것이다. 아니면 디코 성길#0091 ㄱㄱ
            return map;
        }

        throw new CustomException(REFRESH_TOKEN_NOT_FOUND);
    }
}
