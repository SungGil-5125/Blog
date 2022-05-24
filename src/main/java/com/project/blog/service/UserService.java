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

    @Transactional
    public ResponseEntity<FileSystemResource> getProfile_img(Long user_id) throws IOException {

        String profile_image = String.valueOf(findByUser_id(user_id).getPrifile_image());

        Path path = new File("profile/" + profile_image).toPath();
        FileSystemResource resource = new FileSystemResource(path);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
                .body(resource);

    }

    @Transactional
    public String getProfile_name(Long user_id) {

        return findByUser_id(user_id).getName();

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
                .boards(user.getBoard())
                .build();

        return userResponseDto;
    }

    @Transactional
    public void updateProfile(Long user_id, String name, String password, String newPassword, MultipartFile file) throws IOException {

        User user = userRepository.findById(user_id)
                .orElseThrow(()-> new CustomException(USER_NOT_FIND));

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

        String new_file_name = user_id + originalFileExtension;
        user.profile_update(new_file_name);

        folder = new File(absolutePath + path + File.separator + new_file_name);
        file.transferTo(folder);

        }
}

