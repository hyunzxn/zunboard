package com.hyunzxn.zunboard.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.hyunzxn.zunboard.domain.User;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("유저가 정상적으로 저장이 된다.")
	void 유저저장테스트() {
		//given
		User user = User.builder()
			.username("현준")
			.account("hyunzxn")
			.password("1234")
			.build();

		//when
		User savedUser = userRepository.save(user);

		//then
		assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
		assertThat(savedUser.getAccount()).isEqualTo(user.getAccount());
	}

	@Test
	@DisplayName("계정으로 회원을 조회할 수 있다.")
	void 회원조회테스트() {
		//given
		User user = User.builder()
			.username("현준")
			.account("hyunzxn")
			.password("1234")
			.build();

		User savedUser = userRepository.save(user);

		//when
		Optional<User> findUser = userRepository.findByAccount(savedUser.getAccount());

		//then
		assertThat(findUser).isPresent();
	}

}