package com.nathangawith.umkc;

public class Queries {
	public static final String GET_USER = "SELECT * FROM users WHERE username = ?";
	public static final String GET_USER_PASSWORD = "SELECT * FROM users WHERE username = ? AND password = ?";
	public static final String INSERT_USER = "INSERT INTO users (username, password) VALUES (?, ?)";
	public static final String GET_ACCOUNT = "SELECT * FROM accounts WHERE userid = ? AND description = ?";
	public static final String INSERT_ACCOUNT  = "INSERT INTO accounts (userid, description) VALUES (?, ?)";
	public static final String GET_CATEGORY = "SELECT * FROM categories WHERE userid = ? AND type = ? AND description = ?";
	public static final String INSERT_CATEGORY = "INSERT INTO categories (userid, type, description) VALUES (?, ?, ?)";
}
