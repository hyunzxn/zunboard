package com.hyunzxn.zunboard.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequest {

	private String account;
	private String password;

	@Builder
	public LoginRequest(String account, String password) {
		this.account = account;
		this.password = password;
	}
}
