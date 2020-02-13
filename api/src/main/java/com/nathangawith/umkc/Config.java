package com.nathangawith.umkc;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.gson.Gson;
import com.nathangawith.umkc.interceptors.JWTInterceptor;

class ConfigDto {
	public String dbConnectionURL;
	public String dbUser;
	public String dbPassword;
	public String jwtSecretKey;
	public Integer tokenLifespan;
}

@Component
public class Config extends WebMvcConfigurerAdapter {

	public static String dbConnectionURL;
	public static String dbUser;
	public static String dbPassword;
	public static String jwtSecretKey;
	public static Integer tokenLifespan;

	@Autowired
	JWTInterceptor jwtMiddleware;
		
	public Config() {
		super();
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
		registry.addInterceptor(jwtMiddleware);
	}
}
