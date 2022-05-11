package com.project.blog.dto.Request;

import com.project.blog.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ImageDto {

    private String origFileName;
    private String filePath;
    private String fileSize;

    public Image toEntity() {
       return Image.builder()
               .origFileName(origFileName)
               .filePath(filePath)
               .build();
    }
}