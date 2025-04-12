package com.capstone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class CapstoneApplication {

	public static void main(String[] args) {
		System.out.println("GOOGLE_CLIENT_ID = " + System.getenv("GOOGLE_CLIENT_ID"));
		System.out.println("GOOGLE_CLIENT_SECRET = " + System.getenv("GOOGLE_CLIENT_SECRET"));
		System.out.println("IMP_API_KEY = " + System.getenv("IMP_API_KEY"));
		System.out.println("IMP_API_SECRET = " + System.getenv("IMP_API_SECRET"));
		System.out.println("KAKAO_CLIENT_ID = " + System.getenv("KAKAO_CLIENT_ID"));
		System.out.println("KAKAO_CLIENT_SECRET = " + System.getenv("KAKAO_CLIENT_SECRET"));
		System.out.println("MAIL_PASSWORD = " + System.getenv("MAIL_PASSWORD"));
		System.out.println("NAVER_CLIENT_ID = " + System.getenv("NAVER_CLIENT_ID"));
		System.out.println("NAVER_CLIENT_SECRET = " + System.getenv("NAVER_CLIENT_SECRET"));
		System.out.println("OPENAI_API_KEY = " + System.getenv("OPENAI_API_KEY"));
		System.out.println("REDIS_USERNAME = " + System.getenv("REDIS_USERNAME"));
		System.out.println("REDIS_PASSWORD = " + System.getenv("REDIS_PASSWORD"));
		SpringApplication.run(CapstoneApplication.class, args);
	}

}
