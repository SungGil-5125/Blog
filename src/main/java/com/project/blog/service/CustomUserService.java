package com.project.blog.service;

import com.project.blog.exception.ErrorCode;
import com.project.blog.exception.exception.EmailNotFind;
import com.project.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findById(email)
                .orElseThrow(() -> new EmailNotFind("Email is not find", ErrorCode.EMAIL_NOT_FIND));
    }
}
