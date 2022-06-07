package com.project.blog.service;

import com.project.blog.config.jwt.TokenProvider;
import com.project.blog.domain.User;
import com.project.blog.dto.Request.UserLoginDto;
import com.project.blog.dto.Request.UserSignupDto;
import com.project.blog.dto.Request.UserUpdateDto;
import com.project.blog.dto.Response.UserLoginResponseDto;
import com.project.blog.dto.Response.UserResponseDto;
import com.project.blog.exception.CustomException;
import com.project.blog.exception.ErrorCode;
import com.project.blog.repository.UserRepository;
import com.project.blog.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.project.blog.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final CurrentUserUtil currentUserUtil;

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
    public UserLoginResponseDto login(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FIND));

        if(!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_CORRECT);
        }

        final String AccessToken = tokenProvider.generateAccessToken(user.getEmail()); //accessToken 생성
        final String RefreshToken = tokenProvider.generateRefreshToken(user.getEmail()); //freshToken 생성

        user.updateRefreshToken(RefreshToken);

        return UserLoginResponseDto.builder()
                .user_id(user.getUser_id())
                .RefreshToken(RefreshToken)
                .AccessToken(AccessToken)
                .build();

    }

    @Transactional
    public void Logout() {
        User user = CurrentUserUtil();
        user.updateRefreshToken(null);
    }

    @Transactional
    public ResponseEntity<FileSystemResource> getProfile_img() throws IOException {

        User user = CurrentUserUtil();

        String profile_image = String.valueOf(findByUser_id(user.getUser_id()).getPrifile_image());

        Path path = new File("profile/" + profile_image).toPath();
        FileSystemResource resource = new FileSystemResource(path);

        if(!resource.exists()) {
            throw new CustomException(IMAGE_NOT_FOUND);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
                .body(resource);

    }

    @Transactional
    public UserResponseDto getProfile_name() {

        User user = CurrentUserUtil();

        return UserResponseDto.builder()
                .user_id(user.getUser_id())
                .name(user.getName())
                .build();

    }

    @Transactional
    public UserResponseDto findByUser_id(Long user_id) {
        User user = userRepository.findById(user_id)
                .orElseThrow(()-> new CustomException(USER_NOT_FIND));

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .user_id(user_id)
                .email(user.getEmail())
                .name(user.getName())
                .prifile_image(user.getProfile_image())
                .build();

        return userResponseDto;
    }

    public User CurrentUserUtil() {
        return currentUserUtil.getCurrentUser();
    }

    @Transactional
    public void updateProfile(String name, String password, String newPassword, MultipartFile file) throws IOException {

        User user = CurrentUserUtil();

        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .name(name)
                .password(password)
                .newPassword(newPassword)
                .build();

        String new_password_encode = passwordEncoder.encode(userUpdateDto.getNewPassword());

        if(!passwordEncoder.matches(userUpdateDto.getPassword(), user.getPassword())){
            throw new CustomException(PASSWORD_NOT_CORRECT);
        }

        user.update(userUpdateDto.getName(), new_password_encode);

        updateProfile_image(user.getUser_id(), file);

        }

        @Transactional
        public void updateProfile_image(Long user_id, MultipartFile file) throws IOException {

            User user = userRepository.findById(user_id)
                    .orElseThrow(()-> new CustomException(USER_NOT_FIND));

            if(file.isEmpty()) {
                throw new CustomException(IMAGE_NOT_FOUND);
            }

            //사진 업로드
            String absolutePath = new File("").getAbsolutePath() + "\\";
            String path = "profile" + File.separator; //current_date
            File folder = new File(path);

            if(!folder.exists()) {
                folder.mkdirs();
            }

            String originalFileExtension = null;
            String contentType = file.getContentType();

            if(contentType.contains("image/jpeg") || contentType.contains("image/png")) {
                originalFileExtension = ".jpg";
            }

            if(contentType.contains("image/gif")) {
                originalFileExtension = ".gif";
            }

            String new_file_name = user_id + originalFileExtension;
            user.profile_update(new_file_name);

            folder = new File(absolutePath + path + File.separator + new_file_name);
            file.transferTo(folder);


        }
}

