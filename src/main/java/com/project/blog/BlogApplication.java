package com.project.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplication {

//	public static final String APPLICATION_LOCATIONS = "spring.config.location="
//			+ "classpath:application.yml"
//			+ "classpath:aws.yml"; //application.yml 과 aws.yml 두개의 파일 모두를 설정 파일로 읽어서 사용한다.

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

}
