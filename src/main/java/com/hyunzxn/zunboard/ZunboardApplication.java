package com.hyunzxn.zunboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ZunboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZunboardApplication.class, args);
	}

}
