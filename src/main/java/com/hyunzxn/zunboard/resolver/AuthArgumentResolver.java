package com.hyunzxn.zunboard.resolver;

import static org.springframework.http.HttpHeaders.*;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.hyunzxn.zunboard.annotations.Login;
import com.hyunzxn.zunboard.config.JwtConfig;
import com.hyunzxn.zunboard.domain.User;
import com.hyunzxn.zunboard.exception.UnAuthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

	private final JwtConfig jwtConfig;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
		boolean isStringType = String.class.equals(parameter.getParameterType());

		return hasLoginAnnotation && isStringType;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		String jwtToken = webRequest.getHeader(AUTHORIZATION);
		if (jwtToken == null || jwtToken.equals("")) {
			log.info("ArgumentResolver에서 에러가 터지는 상황");
			throw new UnAuthorizedException();
		}

		try {
			// // 만들어 둔 JWT Token 읽는 과정
			Jws<Claims> claims = Jwts.parserBuilder()
				.setSigningKey(jwtConfig.getKey())
				.build()
				.parseClaimsJws(jwtToken);

			User user = User.builder()
				.account(claims.getBody().getSubject())
				.build();

			return user.getAccount();
		} catch (JwtException e) {
			throw new UnAuthorizedException();
		}
	}
}
