package com.hyunzxn.zunboard.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hyunzxn.zunboard.crypto.PasswordEncoder;
import com.hyunzxn.zunboard.domain.User;
import com.hyunzxn.zunboard.exception.AlreadyExistAccountException;
import com.hyunzxn.zunboard.exception.InvalidLoginRequestException;
import com.hyunzxn.zunboard.repository.UserRepository;
import com.hyunzxn.zunboard.request.LoginRequest;
import com.hyunzxn.zunboard.request.SignupRequest;
import com.hyunzxn.zunboard.response.LoginResponse;
import com.hyunzxn.zunboard.util.JwtProvider;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@InjectMocks
	private AuthService authService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtProvider jwtProvider;

	@Test
	@DisplayName("회원가입이 성공한다.")
	void 회원가입성공테스트() {
		//given
		SignupRequest request = SignupRequest.builder()
			.username("현준")
			.account("hyunzxn")
			.password("1234")
			.build();

		User user = User.builder()
			.username(request.getUsername())
			.account(request.getAccount())
			.password(request.getPassword())
			.build();

		given(userRepository.save(any())).willReturn(user);

		//when
		authService.signup(request);

		//then
		assertThat(userRepository.findAll()).isNotNull();
	}

	@Test
	@DisplayName("중복된 아이디가 있어서 회원가입이 되지 않는다.")
	void 회원가입실패테스트() {
		//given
		SignupRequest request = SignupRequest.builder()
			.username("현준")
			.account("hyunzxn")
			.password("1234")
			.build();

		User user = User.builder()
			.username(request.getUsername())
			.account(request.getAccount())
			.password(request.getPassword())
			.build();

		given(userRepository.findByAccount(any())).willReturn(Optional.of(user));

		//when, then
		assertThatThrownBy(() -> authService.signup(request))
			.isInstanceOf(AlreadyExistAccountException.class);
	}

	@Test
	@DisplayName("로그인에 성공한다.")
	void 로그인성공테스트() {
		//given
		User user = User.builder()
			.username("현준")
			.account("hyunzxn")
			.password("1234")
			.build();

		LoginRequest request = LoginRequest.builder()
			.account("hyunzxn")
			.password("1234")
			.build();

		given(userRepository.findByAccount(any())).willReturn(Optional.of(user));
		given(passwordEncoder.matches(request.getPassword(), user.getPassword())).willReturn(true);
		given(jwtProvider.createToken(anyString())).willReturn(anyString());

		//when
		LoginResponse loginResponse = authService.login(request);

		//then
		assertThat(loginResponse.getAccount()).isEqualTo(request.getAccount());
	}

	@Test
	@DisplayName("회원이 존재하지 않아 로그인에 실패한다.")
	void 로그인실패_회원없음_테스트() {
		//given
		LoginRequest request = LoginRequest.builder()
			.account("hyunzxn")
			.password("1234")
			.build();

		given(userRepository.findByAccount(any())).willReturn(Optional.empty());

		//when, then
		assertThatThrownBy(() -> authService.login(request))
			.isInstanceOf(InvalidLoginRequestException.class);
	}

	@Test
	@DisplayName("비밀번호가 일치하지 않아 로그인에 실패한다.")
	void 로그인실패_비밀번호틀림_테스트() {
		//given
		User user = User.builder()
			.username("현준")
			.account("hyunzxn")
			.password("1234")
			.build();

		LoginRequest request = LoginRequest.builder()
			.account("hyunzxn")
			.password("1234")
			.build();

		given(userRepository.findByAccount(any())).willReturn(Optional.of(user));
		given(passwordEncoder.matches(request.getPassword(), user.getPassword())).willReturn(false);

		//when, then
		assertThatThrownBy(() -> authService.login(request))
			.isInstanceOf(InvalidLoginRequestException.class);
	}
	
}