package com.hyunzxn.zunboard.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

	private String account;
	private String token;

	@Builder
	public LoginResponse(String account, String token) {
		this.account = account;
		this.token = token;
	}
}
