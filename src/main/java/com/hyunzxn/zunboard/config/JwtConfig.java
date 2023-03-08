package com.hyunzxn.zunboard.config;

import java.util.Base64;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

	private byte[] key;

	public void setKey(String key) {
		this.key = Base64.getDecoder().decode(key);
	}

	public byte[] getKey() {
		return key;
	}
}
