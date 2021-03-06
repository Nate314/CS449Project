package com.nathangawith.umkc.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.nathangawith.umkc.Config;

@Component
public class JWTInterceptor implements HandlerInterceptor {

	public JWTInterceptor() {
		super();
	}
	
	public static int getUserIDFromHeader(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null) {
			throw new RuntimeException("Authorization header not present.");
		}
		
		String[] parts = authHeader.split("Bearer ");
		if (parts.length == 2) {
			String token = parts[1];
			return JWT.decode(token).getClaim("user_id").asInt();
		}
		throw new RuntimeException("401");
	}

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object object) throws Exception {

		System.out.println(httpServletRequest.getRequestURL());
		System.out.println(httpServletRequest.getRequestURL().indexOf("/auth"));

		if (httpServletRequest.getRequestURL().indexOf("/auth") == -1) {

			String authHeader = httpServletRequest.getHeader("Authorization");

			if (authHeader == null) {
				throw new RuntimeException("Authorization header not present.");
			}

			try {
				String[] parts = authHeader.split("Bearer ");
				if (parts.length == 2) {
					String token = parts[1];
					int iat = JWT.decode(token).getClaim("iat").asInt();
					int exp = JWT.decode(token).getClaim("exp").asInt();
					int now = (int) (System.currentTimeMillis() / 1000);
					if (iat <= now && now <= exp) {

					} else {
						throw new Exception("Expired JWT");
					}

					JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(Config.jwtSecretKey)).build();
					try {
						jwtVerifier.verify(token);
					} catch (JWTVerificationException e) {
						throw new Exception("Incorrectly Signed JWT");
					}
				}
			} catch (JWTDecodeException j) {
				throw new RuntimeException("401");
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {
	}

}
