package com.nathangawith.umkc;

import java.util.Arrays;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.google.gson.Gson;
import com.nathangawith.umkc.interceptors.JWTInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.*;

class ConfigDto {
	public String dbConnectionURL;
	public String dbUser;
	public String dbPassword;
	public String jwtSecretKey;
	public Integer tokenLifespan;
}

@Component
@Configuration
public class Config implements WebMvcConfigurer {

	public static String dbConnectionURL;
	public static String dbUser;
	public static String dbPassword;
	public static String jwtSecretKey;
	public static Integer tokenLifespan;

	@Autowired
	JWTInterceptor jwtMiddleware;
		
	public Config() {
    	List<String> jsonLines;
		try {
			jsonLines = Arrays.asList(FileIO.readFileLines("config.json"));
	    	String json = String.join("\n", jsonLines);
	    	ConfigDto config = new Gson().fromJson(json, ConfigDto.class);
	    	Config.dbConnectionURL = config.dbConnectionURL;
	    	Config.dbUser = config.dbUser;
	    	Config.dbPassword = config.dbPassword;
	    	Config.jwtSecretKey = config.jwtSecretKey;
	    	Config.tokenLifespan = config.tokenLifespan;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// The ping endpoint is the health check, so it must answer without a token.
		registry.addInterceptor(jwtMiddleware).excludePathPatterns("/api/test/ping");
	}

	// The Android app builds URLs like http://host:9090//auth/login. Spring 7 path patterns do not
	// match the double slash and Boot 4 dropped the Ant matcher, so collapse slashes and forward.
	@Bean
	public Filter collapseSlashesFilter() {
		return (request, response, chain) -> {
			String uri = ((HttpServletRequest) request).getRequestURI();
			if (uri.contains("//")) request.getRequestDispatcher(uri.replaceAll("/{2,}", "/")).forward(request, response);
			else chain.doFilter(request, response);
		};
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("http://localhost:8080");
	}
}
