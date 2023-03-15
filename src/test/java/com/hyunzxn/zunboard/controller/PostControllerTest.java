package com.hyunzxn.zunboard.controller;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunzxn.zunboard.domain.Post;
import com.hyunzxn.zunboard.domain.User;
import com.hyunzxn.zunboard.repository.PostRepository;
import com.hyunzxn.zunboard.repository.UserRepository;
import com.hyunzxn.zunboard.request.LoginRequest;
import com.hyunzxn.zunboard.request.PostCreateRequest;
import com.hyunzxn.zunboard.request.PostUpdateRequest;
import com.hyunzxn.zunboard.request.SignupRequest;
import com.hyunzxn.zunboard.response.LoginResponse;
import com.hyunzxn.zunboard.service.AuthService;

import lombok.extern.slf4j.Slf4j;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	private ObjectMapper objectMapper;

	private String jwt;

	@BeforeEach
	void setUp() {
		//JWT 발급을 위한 회원 정보 생성 절차
		SignupRequest signupRequest = SignupRequest.builder()
			.username("test")
			.account("test-account")
			.password("1234")
			.build();
		authService.signup(signupRequest);
		log.info("========회원가입완료=========");

		LoginRequest loginRequest = LoginRequest.builder()
			.account("test-account")
			.password("1234")
			.build();
		LoginResponse loginResponse = authService.login(loginRequest);
		log.info("========로그인 완료=========");
		jwt = loginResponse.getToken();

	}

	@AfterEach
	void clean() {
		postRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("글 작성에 성공한다.")
	void 글작성요청테스트() throws Exception {
		//given
		PostCreateRequest request = PostCreateRequest.builder()
			.title("제목입니다.")
			.content("내용입니다.")
			.build();

		//expected
		mockMvc.perform(post("/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.header(AUTHORIZATION, jwt)
			)
			.andExpect(status().isCreated())
			.andDo(print());
	}

	@Test
	@DisplayName("글 작성 요청 시 인증 안 된 사용자는 401 에러 발생")
	void 미인증사용자_글작성테스트() throws Exception {
		//given
		PostCreateRequest request = PostCreateRequest.builder()
			.title("제목입니다.")
			.content("내용입니다.")
			.build();

		//expected
		mockMvc.perform(post("/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(status().isUnauthorized())
			.andDo(print());
	}

	@Test
	@DisplayName("글 전체 조회에 성공한다.")
	void 글전체조회요청테스트() throws Exception {
		//given
		User user = User.builder()
			.username("현준")
			.account("hyunzxn")
			.password("1234")
			.build();
		userRepository.save(user);

		List<Post> posts = new ArrayList<>();
		for (int i=1; i<11; i++) {
			Post post = Post.createPost("제목 " + i, "내용" + i, user);
			posts.add(post);
		}
		postRepository.saveAll(posts);

		//expected
		mockMvc.perform(get("/posts?page=0&size=5")
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.content.length()").value(5))
			.andExpect(jsonPath("$.data.content[0].postId").value(posts.get(9).getId()))
			.andExpect(jsonPath("$.data.content[0].title").value("제목 10"))
			.andExpect(jsonPath("$.data.content[0].content").value("내용10"))
			.andDo(print());
	}

	@Test
	@DisplayName("글 단건 조회에 성공한다.")
	void 글단건조회요청테스트() throws Exception {
		//given
		User user = User.builder()
			.username("현준")
			.account("hyunzxn")
			.password("1234")
			.build();
		userRepository.save(user);

		Post post = Post.createPost("제목입니다.", "내용입니다.", user);
		postRepository.save(post);

		//expected
		mockMvc.perform(get("/posts/{id}", post.getId())
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("글이 수정된다.")
	void 글수정요청테스트() throws Exception {
		//given
		Post post = Post.builder()
			.title("제목입니다.")
			.content("내용입니다.")
			.build();
		postRepository.save(post);

		PostUpdateRequest request = PostUpdateRequest.builder()
			.updatedTitle("수정된 제목입니다.")
			.updatedContent("수정된 내용입니다.")
			.build();

		//expected
		mockMvc.perform(put("/posts/{id}", post.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.header(AUTHORIZATION, jwt)
			)
			.andExpect(status().isOk())
			.andDo(print());
	}
}