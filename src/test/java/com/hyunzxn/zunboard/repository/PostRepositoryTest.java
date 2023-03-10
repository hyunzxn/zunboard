package com.hyunzxn.zunboard.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.hyunzxn.zunboard.domain.Post;
import com.hyunzxn.zunboard.domain.User;
import com.hyunzxn.zunboard.exception.NotFoundException;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class PostRepositoryTest {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("글이 성공적으로 저장된다.")
	void 글저장테스트() {
		//given
		User user = User.builder()
			.username("테스트")
			.account("Test")
			.password("1234")
			.build();

		Post post = Post.createPost("제목", "내용", user);

		//when
		Post savedPost = postRepository.save(post);

		//then
		assertThat(savedPost.getTitle()).isEqualTo("제목");
		assertThat(savedPost.getContent()).isEqualTo("내용");
		assertThat(savedPost.getUser().getAccount()).isEqualTo("Test");
	}

	@Test
	@DisplayName("전체 글이 성공적으로 조회된다.")
	void 전체글조회테스트() {
		//given
		User user = User.builder()
			.username("테스트")
			.account("Test")
			.password("1234")
			.build();
		userRepository.save(user);

		for (int i=0; i<10; i++) {
			Post post = Post.createPost("제목 " + i, "내용 " + i, user);
			postRepository.save(post);
		}

		PageRequest pageRequest = PageRequest.of(0, 5);

		//when
		Page<Post> pages = postRepository.findAllByOrderByIdDesc(pageRequest);

		//then
		assertThat(pages.getTotalElements()).isEqualTo(10);
		assertThat(pages.getTotalPages()).isEqualTo(2);
	}

	@Test
	@DisplayName("글 단건 조회가 성공한다.")
	void 글단건조회테스트() {
		//given
		User user = User.builder()
			.username("테스트")
			.account("Test")
			.password("1234")
			.build();

		Post post = Post.createPost("제목", "내용", user);
		Post savedPost = postRepository.save(post);

		//when
		Post findPost = postRepository.findById(savedPost.getId()).orElseThrow(NotFoundException::new);

		//then
		assertThat(findPost.getTitle()).isEqualTo("제목");
		assertThat(findPost.getContent()).isEqualTo("내용");
	}

}