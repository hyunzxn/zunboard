package com.hyunzxn.zunboard.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

	private String isSuccess;
	private String message;
	private T data;

	@Builder
	public ApiResponse(String isSuccess, String message, T data) {
		this.isSuccess = isSuccess;
		this.message = message;
		this.data = data;
	}
}
