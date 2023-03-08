package com.hyunzxn.zunboard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hyunzxn.zunboard.exception.RootException;
import com.hyunzxn.zunboard.response.ErrorResponse;

@RestControllerAdvice
public class ExceptionController {

	@ExceptionHandler(RootException.class)
	public ResponseEntity<ErrorResponse> rootExceptionHandler(RootException e) {

		ErrorResponse errorResponse = ErrorResponse.builder()
			.code(e.getStatusCode())
			.message(e.getMessage())
			.validation(e.getValidation())
			.build();

		return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
	}
}
