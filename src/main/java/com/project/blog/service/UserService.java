package com.project.blog.service;

import com.project.blog.config.jwt.TokenProvider;
import com.project.blog.domain.User;
import com.project.blog.dto.Request.UserLoginDto;
import com.project.blog.dto.Request.UserSignupDto;
import com.project.blog.dto.Response.TokenDto;
import com.project.blog.exception.ErrorCode;
import com.project.blog.exception.exception.EmailNotFind;
import com.project.blog.exception.exception.PasswordNotCorrect;
import com.project.blog.exception.exception.UsedEmail;
import com.project.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public User join(UserSignupDto userSignupDto) {
        Optional<User> Email = userRepository.findByEmail(userSignupDto.getEmail());

        if(Email.isPresent()){
            throw new UsedEmail("Email is already used", ErrorCode.USED_EMAIL);
        }

        userSignupDto.setPassword(passwordEncoder.encode(userSignupDto.getPassword()));
        User user = userSignupDto.toEntity();

        return userRepository.save(user);
    }

    @Transactional
    public TokenDto login(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new EmailNotFind("Email is not find", ErrorCode.EMAIL_NOT_FIND));


        if(!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new PasswordNotCorrect("Password is not correct", ErrorCode.PASSWORD_NOT_CORRECT);
        }

        String AccessToken = tokenProvider.createAccessTokenDto(userLoginDto.getEmail());
        String RefreshToken = tokenProvider.createRefreshToken(userLoginDto.getEmail());

        TokenDto tokenDto = TokenDto.builder()
                .AccessToken(AccessToken)
                .RefreshToken(RefreshToken)
                .build();

        return tokenDto;
    }
}
