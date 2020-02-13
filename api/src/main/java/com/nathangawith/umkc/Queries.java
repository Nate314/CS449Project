package com.nathangawith.umkc;

public class Queries {
	public static final String GET_USER = "SELECT * FROM users WHERE username = ?";
	public static final String GET_USER_PASSWORD = "SELECT * FROM users WHERE username = ? AND password = ?";
	public static final String INSERT_USER = "INSERT INTO users (username, password) VALUES (?, ?)";
}
