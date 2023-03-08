package com.hyunzxn.zunboard.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	private String account;

	private String password;

	@Builder
	public User(String username, String account, String password) {
		this.username = username;
		this.account = account;
		this.password = password;
	}

	public static User of(String username, String account, String password) {
		return User.builder()
			.username(username)
			.account(account)
			.password(password)
			.build();
	}
}
