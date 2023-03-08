package com.hyunzxn.zunboard.service;

import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunzxn.zunboard.config.JwtConfig;
import com.hyunzxn.zunboard.crypto.PasswordEncoder;
import com.hyunzxn.zunboard.domain.User;
import com.hyunzxn.zunboard.exception.AlreadyExistAccountException;
import com.hyunzxn.zunboard.exception.InvalidLoginRequestException;
import com.hyunzxn.zunboard.repository.UserRepository;
import com.hyunzxn.zunboard.request.LoginRequest;
import com.hyunzxn.zunboard.request.SignupRequest;
import com.hyunzxn.zunboard.response.LoginResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtConfig jwtConfig;

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

		String token = createToken(request.getAccount());

		return LoginResponse.builder()
			.account(findUser.getAccount())
			.token(token)
			.build();
	}

	private String createToken(String account) {

		SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getKey());

		Claims claims = Jwts.claims().setSubject(account);

		return Jwts.builder()
			.setClaims(claims)
			.signWith(key)
			.setIssuedAt(new Date())
			.compact();
	}
}
