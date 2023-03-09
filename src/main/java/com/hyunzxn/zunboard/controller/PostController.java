package com.hyunzxn.zunboard.controller;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hyunzxn.zunboard.annotations.Login;
import com.hyunzxn.zunboard.request.PostCreateRequest;
import com.hyunzxn.zunboard.response.ApiResponse;
import com.hyunzxn.zunboard.response.PostResponse;
import com.hyunzxn.zunboard.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping("/posts")
	public ResponseEntity<Long> createPost(@RequestBody PostCreateRequest request, @Login String account) {
		return ResponseEntity.status(CREATED).body(postService.createPost(request, account));
	}

	@GetMapping("/posts")
	public ResponseEntity<ApiResponse<Object>> getAllPosts() {
		List<PostResponse> posts = postService.getAllPosts();
		ApiResponse<Object> result = ApiResponse.builder()
			.isSuccess("true")
			.message("글 전체 조회 성공")
			.data(posts)
			.build();
		return ResponseEntity.status(OK).body(result);
	}
}
