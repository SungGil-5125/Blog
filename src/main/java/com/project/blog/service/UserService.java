package com.project.blog.service;

import com.project.blog.domain.User;
import com.project.blog.dto.User.UserSignupDto;
import com.project.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User join(UserSignupDto userSignupDto) {
        Optional<User> byEmail = userRepository.findByEmail(userSignupDto.getEmail());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        userSignupDto.setPassword(encoder.encode(userSignupDto.getPassword()));
        User user = userSignupDto.toEntity();

        return userRepository.save(user);
    }


}
