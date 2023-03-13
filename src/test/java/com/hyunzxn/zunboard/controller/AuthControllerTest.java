package com.hyunzxn.zunboard.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunzxn.zunboard.crypto.PasswordEncoder;
import com.hyunzxn.zunboard.domain.User;
import com.hyunzxn.zunboard.repository.UserRepository;
import com.hyunzxn.zunboard.request.LoginRequest;
import com.hyunzxn.zunboard.request.SignupRequest;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void clean() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("회원가입 POST 요청")
	void 회원가입요청테스트() throws Exception {
		//given
		SignupRequest request = SignupRequest.builder()
			.username("현준")
			.account("hyunzxn")
			.password("1234")
			.build();

		//expected
		mockMvc.perform(post("/auth/signup")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
		)
			.andExpect(status().isCreated())
			.andDo(print());
	}

	@Test
	@DisplayName("로그인 POST 요청")
	void 로그인요청테스트() throws Exception {
		//given
		User user = User.builder()
			.username("현준")
			.account("hyunzxn")
			.password(passwordEncoder.encrypt("1234"))
			.build();
		userRepository.save(user);

		LoginRequest request = LoginRequest.builder()
			.account("hyunzxn")
			.password("1234")
			.build();

		//expected
		mockMvc.perform(post("/auth/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
		)
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("응답으로 JWT가 나간다.")
	void JWT_응답테스트() throws Exception {
		//given
		User user = User.builder()
			.username("현준")
			.account("hyunzxn")
			.password(passwordEncoder.encrypt("1234"))
			.build();
		userRepository.save(user);

		LoginRequest request = LoginRequest.builder()
			.account("hyunzxn")
			.password("1234")
			.build();

		//expected
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token", Matchers.notNullValue()))
			.andDo(print());
	}

}