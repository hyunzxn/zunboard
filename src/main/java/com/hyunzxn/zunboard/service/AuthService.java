package com.hyunzxn.zunboard.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunzxn.zunboard.crypto.PasswordEncoder;
import com.hyunzxn.zunboard.domain.User;
import com.hyunzxn.zunboard.exception.AlreadyExistAccountException;
import com.hyunzxn.zunboard.exception.InvalidLoginRequestException;
import com.hyunzxn.zunboard.repository.UserRepository;
import com.hyunzxn.zunboard.request.LoginRequest;
import com.hyunzxn.zunboard.request.SignupRequest;
import com.hyunzxn.zunboard.response.LoginResponse;
import com.hyunzxn.zunboard.util.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	@Transactional
	public Long signup(SignupRequest request) {

		Optional<User> findUser = userRepository.findByAccount(request.getAccount());

		if (findUser.isPresent()) {
			throw new AlreadyExistAccountException();
		}

		User user = User.builder()
			.username(request.getUsername())
			.account(request.getAccount())
			.password(passwordEncoder.encrypt(request.getPassword()))
			.build();
		User savedUser = userRepository.save(user);

		return savedUser.getId();

	}

	public LoginResponse login(LoginRequest request) {

		User findUser = userRepository.findByAccount(request.getAccount())
			.orElseThrow(InvalidLoginRequestException::new);

		boolean isMatched = passwordEncoder.matches(request.getPassword(), findUser.getPassword());
		if (!isMatched) {
			throw new InvalidLoginRequestException();
		}

		String token = jwtProvider.createToken(request.getAccount());

		return LoginResponse.builder()
			.account(findUser.getAccount())
			.token(token)
			.build();
	}
}
