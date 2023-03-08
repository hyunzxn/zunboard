package com.hyunzxn.zunboard.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunzxn.zunboard.domain.User;
import com.hyunzxn.zunboard.exception.AlreadyExistAccountException;
import com.hyunzxn.zunboard.exception.InvalidLoginRequestException;
import com.hyunzxn.zunboard.repository.UserRepository;
import com.hyunzxn.zunboard.request.LoginRequest;
import com.hyunzxn.zunboard.request.SignupRequest;
import com.hyunzxn.zunboard.response.LoginResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final UserRepository userRepository;

	@Value("${jwt.secret.key}")
	private String salt;

	@Transactional
	public Long signup(SignupRequest request) {

		User user = User.of(request.getUsername(), request.getAccount(), request.getPassword());
		Optional<User> findUser = userRepository.findByAccount(request.getAccount());

		if (findUser.isPresent()) {
			throw new AlreadyExistAccountException();
		} else {
			User savedUser = userRepository.save(user);
			return savedUser.getId();
		}
	}

	public LoginResponse login(LoginRequest request) {

		User findUser = userRepository.findByAccountAndPassword(request.getAccount(), request.getPassword())
			.orElseThrow(InvalidLoginRequestException::new);

		String token = createToken(request.getAccount());

		return LoginResponse.builder()
			.account(findUser.getAccount())
			.token(token)
			.build();
	}

	private String createToken(String account) {

		Key secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
		Claims claims = Jwts.claims().setSubject(account);
		Date now = new Date();
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}
}
