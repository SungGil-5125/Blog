package com.project.blog.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.project.blog.config.AmazonS3.AmazonS3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Config amazonS3Config;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile file, String dirName) throws IOException {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try(InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, dirName + fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IllegalStateException("파일 업로드에 실패했습니다.");
        }

        return fileName;
    }

    public void deleteFile(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    public String createFileName(String fileName) { // 파일 업로드시, 파일명을 난수화하기 위해 random 으로 UUID 를 만들어준다.
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    public String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
            // file 형식이 잘못된 경우를 확인하기 위해 만들었으며, 파일 타입과 상관없이 업로드 할 수 있게 .의 존재 유무만 판단하였다.
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalStateException("잘못된 파일 형식의 파일(" + fileName + ") 입니다.");
        }
    }

}
