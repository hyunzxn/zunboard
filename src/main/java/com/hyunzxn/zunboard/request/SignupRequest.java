package com.hyunzxn.zunboard.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupRequest {

	private String username;
	private String account;
	private String password;

	@Builder
	public SignupRequest(String username, String account, String password) {
		this.username = username;
		this.account = account;
		this.password = password;
	}
}
