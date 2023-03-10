package com.hyunzxn.zunboard.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunzxn.zunboard.domain.Post;
import com.hyunzxn.zunboard.domain.User;
import com.hyunzxn.zunboard.exception.NotFoundException;
import com.hyunzxn.zunboard.exception.UnAuthorizedException;
import com.hyunzxn.zunboard.repository.PostRepository;
import com.hyunzxn.zunboard.repository.UserRepository;
import com.hyunzxn.zunboard.request.PostCreateRequest;
import com.hyunzxn.zunboard.request.PostUpdateRequest;
import com.hyunzxn.zunboard.response.PostResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Transactional
	public Long createPost(PostCreateRequest request, String account) {

		User user = userRepository.findByAccount(account)
			.orElseThrow(UnAuthorizedException::new);

		Post post = Post.createPost(request.getTitle(),request.getContent(), user);

		Post savedPost = postRepository.save(post);

		return savedPost.getId();
	}

	public Page<PostResponse> getAllPosts(Pageable pageable) {
		return postRepository.findAllByOrderByIdDesc(pageable).map(PostResponse::changeToDto);
	}

	public PostResponse getSinglePostById(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(NotFoundException::new);

		return PostResponse.changeToDto(post);
	}

	@Transactional
	public Long updatePost(Long postId, PostUpdateRequest request) {

		Post post = postRepository.findById(postId)
			.orElseThrow(NotFoundException::new);

		post.update(request);
		return post.getId();
	}

	@Transactional
	public void deletePost(Long postId) {

		Optional<Post> findPost = postRepository.findById(postId);
		if (findPost.isEmpty()) {
			throw new NotFoundException();
		}
		postRepository.deleteById(postId);

	}
}
