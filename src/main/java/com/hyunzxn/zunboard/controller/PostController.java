package com.hyunzxn.zunboard.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public ResponseEntity<ApiResponse<Object>> getAllPosts(Pageable pageable) {
		Page<PostResponse> pages = postService.getAllPosts(pageable);
		ApiResponse<Object> response = ApiResponse.builder()
			.isSuccess("true")
			.message("글 전체 조회 성공")
			.data(pages)
			.build();
		return ResponseEntity.status(OK).body(response);
	}
}
