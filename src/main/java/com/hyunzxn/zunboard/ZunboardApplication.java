package com.hyunzxn.zunboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.hyunzxn.zunboard.config.JwtConfig;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(JwtConfig.class)
public class ZunboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZunboardApplication.class, args);
	}

}
