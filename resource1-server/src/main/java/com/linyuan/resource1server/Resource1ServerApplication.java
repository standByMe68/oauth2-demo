package com.linyuan.resource1server;

import com.linyuan.oauth2config.config.annotation.EnableRemoteTokenService;
import com.linyuan.oauth2config.config.annotation.EnableResJWTTokenStore;
import com.linyuan.oauth2config.config.store.ResJWTTokenStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Import(ResJWTTokenStore.class) //OAuth2 使用 JWT 解析令牌
public class Resource1ServerApplication {

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(Resource1ServerApplication.class, args);
	}
}
