package com.project.blog.service.Handler;

import com.project.blog.domain.Image;
import com.project.blog.dto.Request.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;

// 쉬프트 fn 왼쪽 한줄 선택
//@Builder
@Component
public class FIleHandler {

    @Value("servlet.multipart.location")
    private String uploadDir;

    public List<Image> parseFileInfo(List<MultipartFile> multipartFiles) throws IOException {
        //변환한 파일 리스트
        List<Image> fileList = new ArrayList<>();

        //전달되어 온 파일이 존재할 경우
        if(!CollectionUtils.isEmpty(multipartFiles)){

            //파일명을 업로드 한 날짜로 변경하여 저장
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            String current_date = now.format(dateTimeFormatter);

            // 프로젝트 디렉터리 내의 저장을 위한 절대경로 설정
            // 경로 구분자 File.separator 사용
            String absoulutePath = new File("").getAbsolutePath() + File.separator + File.separator;



            //파일을 저장할 세부 경로 지정
            // String path = "C" + File.separator + "Users" + File.separator + "user" + File.separator + "Pictures" + File.separator + "blog";
            String path = "image" + File.separator + current_date;

            File file = new File(uploadDir);

            // 디렉터리가 존재하지 않을 경우
            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                //디렉터리 생성에 실패했을 경우
                if(!wasSuccessful)
                    System.out.println("file: was not Successful");
            }

            for(MultipartFile multipartFile : multipartFiles) {

                String originalFileExtension;
                String contentType = multipartFile.getContentType();


                //확장자 명이 존재 하지 않을 경우 처리 x
                if(ObjectUtils.isEmpty(contentType)){
                    break;
                }

                else {
                    if(contentType.contains("image/jpeg"))
                        originalFileExtension = ".jpg";
                    else if(contentType.contains("image/png"))
                        originalFileExtension = ".png";
                    else
                        //System.out.println("안됨");
                        break;
                }

                // 파일명 중복 피하기 위해 나노 초 까지 지정
                String new_file_name = System.nanoTime() + originalFileExtension;

                ImageDto imageDto = ImageDto.builder()
                        .origFileName(multipartFile.getOriginalFilename())
                        .filePath(path + File.separator + new_file_name)
                        .build();

                Image image = imageDto.toEntity();

                //생성후 리스트에 추가
                fileList.add(image);

                // 업로드 한 파일 데이터를 저장한 파일에 저장
                file = new File(absoulutePath + "C:\\Users\\user\\Pictures\\blog" + File.separator + new_file_name);
                multipartFile.transferTo(file);

                // 파일 권한 설정(쓰기, 읽기)
                file.setWritable(true);
                file.setReadable(true);
            }
        }

        return fileList;
    }

}
