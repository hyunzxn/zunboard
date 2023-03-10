package com.hyunzxn.zunboard.exception;

public class NotFoundException extends RootException {

	private static final String MESSAGE = "존재하지 않는 글입니다. 글 ID를 다시 확인해주세요.";

	public NotFoundException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 400;
	}
}
