package com.hyunzxn.zunboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyunzxn.zunboard.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByAccount(String account);
}
