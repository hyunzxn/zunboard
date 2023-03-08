package com.hyunzxn.zunboard.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.hyunzxn.zunboard.exception.UnAuthorizedException;

public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {

		String authorization = request.getHeader("Authorization");
		if (authorization == null || authorization.equals("")) {
			throw new UnAuthorizedException();
		}
		return true;
	}
}
