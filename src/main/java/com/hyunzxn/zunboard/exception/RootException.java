package com.hyunzxn.zunboard.exception;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public abstract class RootException extends RuntimeException {

	public RootException(String message) {
		super(message);
	}

	public abstract int getStatusCode();

	private final Map<String, String> validation = new HashMap<>();

	public void addValidation(String fieldName, String message) {
		validation.put(fieldName, message);
	}
}
