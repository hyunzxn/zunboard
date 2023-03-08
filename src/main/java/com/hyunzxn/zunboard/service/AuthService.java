package com.hyunzxn.zunboard.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunzxn.zunboard.domain.User;
import com.hyunzxn.zunboard.exception.AlreadyExistAccountException;
import com.hyunzxn.zunboard.repository.UserRepository;
import com.hyunzxn.zunboard.request.SignupRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final UserRepository userRepository;

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
}
