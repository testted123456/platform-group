package com.nonobank.group;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
@EnableEurekaClient
@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
public class PlatformGroupApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformGroupApplication.class, args);
	}
}
