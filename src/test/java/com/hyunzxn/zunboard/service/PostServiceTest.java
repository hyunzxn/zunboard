package com.hyunzxn.zunboard.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.hyunzxn.zunboard.domain.Post;
import com.hyunzxn.zunboard.domain.User;
import com.hyunzxn.zunboard.exception.NotFoundException;
import com.hyunzxn.zunboard.exception.UnAuthorizedException;
import com.hyunzxn.zunboard.repository.PostRepository;
import com.hyunzxn.zunboard.repository.UserRepository;
import com.hyunzxn.zunboard.request.PostCreateRequest;
import com.hyunzxn.zunboard.request.PostUpdateRequest;
import com.hyunzxn.zunboard.response.PostResponse;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

	@InjectMocks
	private PostService postService;

	@Mock
	private PostRepository postRepository;

	@Mock
	private UserRepository userRepository;

	@Test
	@DisplayName("글 저장이 성공적으로 된다.")
	void 글저장테스트() {
		//given
		PostCreateRequest request = PostCreateRequest.builder()
			.title("제목")
			.content("내용")
			.build();

		User user = User.builder()
			.username("테스트")
			.account("Test")
			.password("1234")
			.build();

		Post post = Post.createPost("제목", "내용", user);
		post.setIdForTest(1L);

		given(userRepository.findByAccount(any())).willReturn(Optional.of(user));
		given(postRepository.save(any(Post.class))).willReturn(post);

		//when
		Long savedPostId = postService.createPost(request, user.getAccount());

		//then
		assertThat(savedPostId).isEqualTo(1L);
	}

	@Test
	@DisplayName("인증이 되지 않은 사용자는 글을 작성할 수 없다.")
	void 미인증사용자_글작성불가테스트() {
		//given
		PostCreateRequest request = PostCreateRequest.builder()
			.title("제목")
			.content("내용")
			.build();

		User user = User.builder()
			.username("테스트")
			.account("Test")
			.password("1234")
			.build();

		given(userRepository.findByAccount(any())).willReturn(Optional.empty());

		//when, then
		assertThatThrownBy(() -> postService.createPost(request, user.getAccount()))
			.isInstanceOf(UnAuthorizedException.class);

	}

	@Test
	@DisplayName("저장한 글 전체를 조회할 수 있다.")
	void 전체글조회테스트() {
		//given
		User user = User.builder()
			.username("테스트")
			.account("Test")
			.password("1234")
			.build();

		List<Post> posts = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			Post post = Post.createPost("제목 " + i, "내용 " + i, user);
			posts.add(post);
		}

		PageRequest pageRequest = PageRequest.of(0, 5);
		Page<Post> postPages = new PageImpl<>(posts, pageRequest, posts.size());

		given(postRepository.findAllByOrderByIdDesc(pageRequest)).willReturn(postPages);

		//when
		Page<PostResponse> result = postService.getAllPosts(pageRequest);

		//then
		assertThat(result.getTotalElements()).isEqualTo(10);
		assertThat(result.getTotalPages()).isEqualTo(2);
	}

	@Test
	@DisplayName("글 단건 조회를 할 수 있다.")
	void 글단건조회테스트() {
		//given
		User user = User.builder()
			.username("테스트")
			.account("Test")
			.password("1234")
			.build();

		Post post = Post.createPost("제목", "내용", user);
		post.setIdForTest(1L);

		given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));

		//when
		PostResponse response = postService.getSinglePostById(post.getId());

		//then
		assertThat(response.getTitle()).isEqualTo("제목");
		assertThat(response.getContent()).isEqualTo("내용");
	}

	@Test
	@DisplayName("글의 제목 또는 내용을 수정할 수 있다.")
	void 글수정테스트() {
		//given
		User user = User.builder()
			.username("테스트")
			.account("Test")
			.password("1234")
			.build();

		PostUpdateRequest request = PostUpdateRequest.builder()
			.updatedTitle("수정된 제목")
			.updatedContent("수정된 내용")
			.build();

		Post post = Post.createPost("제목", "내용", user);
		post.setIdForTest(1L);

		given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));

		//when
		Long postId = postService.updatePost(post.getId(), request);

		//then
		assertThat(postId).isEqualTo(1L);
	}

	@Test
	@DisplayName("글을 삭제할 수 있다.")
	void 글삭제테스트() {
		//given
		User user = User.builder()
			.username("테스트")
			.account("Test")
			.password("1234")
			.build();

		PostUpdateRequest request = PostUpdateRequest.builder()
			.updatedTitle("수정된 제목")
			.updatedContent("수정된 내용")
			.build();

		Post post = Post.createPost("제목", "내용", user);
		post.setIdForTest(1L);

		given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));

		//when
		Long postId = postService.deletePost(post.getId());

		//then
		assertThat(postId).isEqualTo(1L);
	}

	@Test
	@DisplayName("글 단건 조회, 수정, 삭제 시 조회에 실패한다.")
	void 글조회_실패테스트() {
		//given
		given(postRepository.findById(any(Long.class))).willReturn(Optional.empty());

		//when, then
		assertThatThrownBy(() -> postService.getSinglePostById(1L))
			.isInstanceOf(NotFoundException.class);
	}

}