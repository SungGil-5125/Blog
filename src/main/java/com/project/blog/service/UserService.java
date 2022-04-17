package com.project.blog.service;

import com.project.blog.domain.User;
import com.project.blog.dto.User.UserLoginDto;
import com.project.blog.dto.User.UserSignupDto;
import com.project.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User join(UserSignupDto userSignupDto) {
        Optional<User> byEmail = userRepository.findByEmail(userSignupDto.getEmail());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        userSignupDto.setPassword(encoder.encode(userSignupDto.getPassword()));
        User user = userSignupDto.toEntity();

        return userRepository.save(user);
    }

    public String login(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 email입니다."));

        if(!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
                throw new IllegalAccessException("잘못된 비밀번호 입니다.");
        }
    }
}
