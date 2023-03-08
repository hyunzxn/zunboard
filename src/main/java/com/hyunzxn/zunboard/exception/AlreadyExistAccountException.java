package com.hyunzxn.zunboard.exception;

import lombok.Getter;

@Getter
public class AlreadyExistAccountException extends RootException {

	private static final String MESSAGE = "이미 존재하는 계정입니다.";

	@Override
	public int getStatusCode() {
		return 400;
	}

	public AlreadyExistAccountException() {
		super(MESSAGE);
	}
}
