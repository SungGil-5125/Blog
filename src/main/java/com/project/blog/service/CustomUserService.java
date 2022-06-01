package com.project.blog.service;

import com.project.blog.exception.CustomException;
import com.project.blog.exception.ErrorCode;
import com.project.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

    // principal "누구"에 해당하는 정보이다. userDetailService 에서 리턴한 객체가 정보가 되는거 같다.
    // principal 의 객체 타입은 userDetails 이다.

    // user 정보를 UserDetails 타입으로 가져오는 DAO 인터페이스 이다.
    // user 정보를 SpringSecurity 한테 제공하는 역할을 한다.
    // 실제로 검증을 하는 것은 AuthenticationManager 이다.

    // 인증을 했다는 것은 userDetail에 들어온걸로 보면 되나?

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USED_EMAIL));
    }

}
