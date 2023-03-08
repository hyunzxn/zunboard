package com.hyunzxn.zunboard.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.hyunzxn.zunboard.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtProvider {

	private final JwtConfig jwtConfig;

	public String createToken(String account) {

		SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getKey());

		Claims claims = Jwts.claims().setSubject(account);

		return Jwts.builder()
			.setClaims(claims)
			.signWith(key)
			.setIssuedAt(new Date())
			.compact();
	}
}
