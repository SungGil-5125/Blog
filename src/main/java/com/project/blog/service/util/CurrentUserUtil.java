package com.project.blog.service.util;

import com.project.blog.domain.User;
import com.project.blog.exception.CustomException;
import com.project.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static com.project.blog.exception.ErrorCode.USER_NOT_FIND;

@Component
@Slf4j
@RequiredArgsConstructor
public class CurrentUserUtil {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        String email = null;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            email = principal.toString();
        }

//        String userEamil = ((UserDetails)principal).getUsername();

//        log.info("userEmail : " + userEamil);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(USER_NOT_FIND));
    }


}
