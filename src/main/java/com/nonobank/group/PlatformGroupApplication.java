package com.nonobank.group;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
public class PlatformGroupApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformGroupApplication.class, args);
	}
}
