package com.nathangawith.umkc;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class API {
   
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(API.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "9090"));
        app.run(args);
	}
}
