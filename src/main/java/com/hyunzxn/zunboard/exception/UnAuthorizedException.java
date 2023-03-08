package com.hyunzxn.zunboard.exception;

public class UnAuthorizedException extends RootException {

	private static final String MESSAGE = "인증되지 않은 사용자 요청입니다.";

	@Override
	public int getStatusCode() {
		return 401;
	}

	public UnAuthorizedException() {
		super(MESSAGE);
	}
}
