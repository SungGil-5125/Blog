package com.project.blog.service;

import com.project.blog.config.jwt.TokenProvider;
import com.project.blog.domain.Image;
import com.project.blog.domain.User;
import com.project.blog.dto.Request.ImageDto;
import com.project.blog.dto.Request.UserLoginDto;
import com.project.blog.dto.Request.UserSignupDto;
import com.project.blog.dto.Request.UserUpdateDto;
import com.project.blog.dto.Response.TokenResponseDto;
import com.project.blog.dto.Response.UserResponseDto;
import com.project.blog.exception.CustomException;
import com.project.blog.exception.ErrorCode;
import com.project.blog.repository.ImageRepository;
import com.project.blog.repository.UserRepository;
import com.project.blog.service.Handler.FIleHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.project.blog.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final FIleHandler fIleHandler;
    private final ImageRepository imageRepository;

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
                .User_id(user_id)
                .User_email(user.getEmail())
                .User_name(user.getName())
                .build();

        return userResponseDto;
    }

    @Transactional
    public void updateProfile(Long user_id, UserUpdateDto userUpdateDto, MultipartFile file) throws IOException {

        User user = userRepository.findById(user_id)
                .orElseThrow(()-> new CustomException(USER_NOT_FIND));

        if(Objects.equals(userUpdateDto.getPassword(), user.getPassword())) {
            throw new CustomException(SAME_PASSWORD);
        }

        //사진 업로드
        String absolutePath = new File("").getAbsolutePath() + "\\";

        String path = "profile" + File.separator; //current_date

        File folder = new File(path);

        if(!folder.exists()) {
            folder.mkdirs();
        }

        String originalFileExtension;
        String contentType = file.getContentType();

        if(contentType.contains("image/jpeg"))
            originalFileExtension = ".jpg";
        else if(contentType.contains("image/png"))
            originalFileExtension = ".png";

        String new_file_name = String.valueOf(user_id);

//            ImageDto imageDto = ImageDto.builder()
//                    .origFileName(file.getOriginalFilename())
//                    .filePath(path + File.separator + new_file_name)
//                    .build();
//
//            Image image = imageDto.toEntity();

            folder = new File(absolutePath + path + File.separator + new_file_name);
            file.transferTo(folder);

//            folder.setWritable(true);
//            folder.setReadable(true);
        }

    }

