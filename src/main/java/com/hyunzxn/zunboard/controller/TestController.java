package com.hyunzxn.zunboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyunzxn.zunboard.annotations.Login;

@RestController
public class TestController {

	@GetMapping("/test")
	public String test(@Login String account) {
		return account;
	}
}
