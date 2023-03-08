package com.hyunzxn.zunboard.exception;

import lombok.Getter;

@Getter
public class InvalidLoginRequestException extends RootException {

	private static final String MESSAGE = "잘못된 아이디 또는 비밀번호 입니다.";

	@Override
	public int getStatusCode() {
		return 400;
	}

	public InvalidLoginRequestException() {
		super(MESSAGE);
	}
}
