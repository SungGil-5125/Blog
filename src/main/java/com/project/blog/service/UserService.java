package com.project.blog.service;

import com.project.blog.config.jwt.TokenProvider;
import com.project.blog.domain.User;
import com.project.blog.dto.Request.UserLoginDto;
import com.project.blog.dto.Request.UserSignupDto;
import com.project.blog.dto.Request.UserUpdateDto;
import com.project.blog.dto.Response.TokenResponseDto;
import com.project.blog.dto.Response.UserResponseDto;
import com.project.blog.exception.CustomException;
import com.project.blog.exception.ErrorCode;
import com.project.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

import static com.project.blog.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Value("servlet.multipart.location")
    private String uploadDir;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public User join(UserSignupDto userSignupDto) {
        Optional<User> Email = userRepository.findByEmail(userSignupDto.getEmail());

        if(Email.isPresent()){
            throw new CustomException(USED_EMAIL);
        }

        userSignupDto.setPassword(passwordEncoder.encode(userSignupDto.getPassword()));
        User user = userSignupDto.toEntity();

        return userRepository.save(user);
    }

    @Transactional
    public TokenResponseDto login(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FIND));


        if(!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_CORRECT);
        }

        String AccessToken = tokenProvider.createAccessTokenDto(userLoginDto.getEmail());
        String RefreshToken = tokenProvider.createRefreshToken(userLoginDto.getEmail());

        return TokenResponseDto.builder()
                .AccessToken(AccessToken)
                .RefreshToken(RefreshToken)
                .build();

    }

    public UserResponseDto profile(Long user_id) {

        User user = userRepository.findById(user_id)
                .orElseThrow(()-> new CustomException(USER_NOT_FIND));

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .Success(true)
                .User_id(user_id)
                .User_email(user.getEmail())
                .User_name(user.getName())
                .build();

        return userResponseDto;
    }


    public boolean UpdateProfile(MultipartFile image) throws Exception {

            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdir();

            if(image.isEmpty()) {
                throw new CustomException(IMAGE_NOT_FOUND);
            }

//            File destination = new File(uploadDir + File.separator + image.getOriginalFilename());
        String fullName = uploadDir+image.getOriginalFilename();
        image.transferTo(new File(fullName));

            System.out.println("이미지 이름 = " + image.getOriginalFilename());


        return true;
    }


}
