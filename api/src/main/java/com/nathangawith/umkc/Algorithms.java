package com.nathangawith.umkc;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Algorithms {
	// https://github.com/patrickfav/bcrypt
	public static String encryptPassword(String username, String password) {
		String usernameChars = username.substring(0, Math.min(username.length(), 16));
		String passwordChars = password.substring(0, Math.min(password.length(), 16 - usernameChars.length()));
		String paddingChars = "qVHD8w*^g@T*2X3Y".substring(usernameChars.length() + passwordChars.length());
		byte[] salt = (usernameChars + passwordChars + paddingChars).getBytes();
		byte[] bcryptHashString = BCrypt.withDefaults().hash(12, salt, password.getBytes(StandardCharsets.UTF_8));
		return new String(bcryptHashString);
	}
	
	// https://www.codevoila.com/post/65/java-json-tutorial-and-example-json-java-orgjson
	public static <T extends Object> String toJSONObject(T object) {
    	JSONObject jsonArray = new JSONObject(object);
    	return jsonArray.toString();
	}
	
	// https://www.codevoila.com/post/65/java-json-tutorial-and-example-json-java-orgjson
	public static <T extends Object> String toJSONArray(Collection<T> array) {
    	JSONArray jsonArray = new JSONArray(array);
    	return jsonArray.toString();
	}
}
