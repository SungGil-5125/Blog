package com.project.blog.service;

import com.project.blog.domain.User;
import com.project.blog.dto.Request.UserLoginDto;
import com.project.blog.dto.Request.UserSignupDto;
import com.project.blog.exception.ErrorCode;
import com.project.blog.exception.exception.EmailNotFind;
import com.project.blog.exception.exception.PasswordNotCorrect;
import com.project.blog.exception.exception.UsedEmail;
import com.project.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User join(UserSignupDto userSignupDto) {
        Optional<User> Email = userRepository.findByEmail(userSignupDto.getEmail());

        if(!Email.isEmpty()){
            throw new UsedEmail("Email is aready used", ErrorCode.USED_EMAIL);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        userSignupDto.setPassword(encoder.encode(userSignupDto.getPassword()));
        User user = userSignupDto.toEntity();

        return userRepository.save(user);
    }

    @Transactional
    public String login(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new EmailNotFind("Email is not find",ErrorCode.EMAIL_NOT_FIND));

        if(!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new PasswordNotCorrect("Password is not correct",ErrorCode.PASSWORD_NOT_CORRECT);
        }

        
    }
}
