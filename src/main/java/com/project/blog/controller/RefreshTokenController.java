package com.project.blog.controller;

import com.project.blog.config.security.jwt.TokenProvider;
import com.project.blog.dto.Request.RefreshTokenDto;
import com.project.blog.response.ResponseService;
import com.project.blog.response.result.CommonResultResponse;
import com.project.blog.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final ResponseService responseService;
    private final TokenProvider tokenProvider;

    // refreshToken 재발급
    @PutMapping("refreshToken")
    public CommonResultResponse reissueRefreshToken(HttpServletRequest request, @RequestBody RefreshTokenDto refreshTokenDto) {
        refreshTokenService.refreshToken(tokenProvider.getRefreshToken(request), refreshTokenDto);
        return responseService.getSuccessResult();
    }


}
