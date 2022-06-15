package com.project.blog.service;

import com.project.blog.config.jwt.TokenProvider;
import com.project.blog.domain.Board;
import com.project.blog.domain.User;
import com.project.blog.dto.Request.UserLoginDto;
import com.project.blog.dto.Request.UserSignupDto;
import com.project.blog.dto.Request.UserUpdateDto;
import com.project.blog.dto.Response.UserLoginResponseDto;
import com.project.blog.dto.Response.UserResponseDto;
import com.project.blog.exception.CustomException;
import com.project.blog.exception.ErrorCode;
import com.project.blog.repository.BoardRepository;
import com.project.blog.repository.UserRepository;
import com.project.blog.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final S3Service s3Service;
    private final BoardRepository boardRepository;

    @Value("${cloud.aws.s3.profile_dir}")
    private String dir;

    // 회원가입
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

    // 로그인
    @Transactional
    public UserLoginResponseDto login(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

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

    // 로그아웃
    @Transactional
    public void Logout() {
        User user = CurrentUserUtil();
        user.updateRefreshToken(null);
    }

    // 프로필 수정
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

        String uploadUrl = s3Service.upload(file, dir);

        user.updateUrl("https://devlog-s3-bucket.s3.ap-northeast-2.amazonaws.com/profile_image/" + uploadUrl);

    }

//    // user_id로 user 정보 보기
//    @Transactional
//    public UserResponseDto get

    // 회원 정보 가져 오기
    @Transactional
    public UserResponseDto getMyProfileImage(Long user_id) {

        User user = userRepository.findById(user_id)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));
        List<Board> boards = boardRepository.findByUser_Id(user_id);
        String profileUrl = user.getUrl();

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .user_id(user_id)
                .email(user.getEmail())
                .name(user.getName())
                .url(user.getUrl())
                .board_number(boards.size())
                .build();

        if(profileUrl == null) {
             userResponseDto.setUrl("https://devlog-s3-bucket.s3.ap-northeast-2.amazonaws.com/profile_image/IMG_5713.jpg");
        }

        return userResponseDto;
    }

    @Transactional
    public String getProfileImage() {
        User user = CurrentUserUtil();

        String url = user.getUrl();

        if(url == null) {
            url = "https://devlog-s3-bucket.s3.ap-northeast-2.amazonaws.com/board_image/%EB%B8%94%EB%A1%9C%EA%B7%B8+%EA%B8%B0%EB%B3%B8+%EC%9D%B4%EB%AF%B8%EC%A7%80.jpg";
        }

        return url;
    }

    // 전체 게시글에 회원 이미지 보여주기
    @Transactional
    public String getProfileImage(Long user_id) {

        User user = userRepository.findById(user_id)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));

        String profileUrl = user.getUrl();

        if(profileUrl == null) {
            return "https://devlog-s3-bucket.s3.ap-northeast-2.amazonaws.com/profile_image/IMG_5713.jpg";
        }



        return profileUrl;
    }

    // 토큰으로 현재 user 가져오기
    public User CurrentUserUtil() {
        return currentUserUtil.getCurrentUser();
    }

}

