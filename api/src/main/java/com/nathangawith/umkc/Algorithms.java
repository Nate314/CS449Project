package com.nathangawith.umkc;

import java.lang.reflect.Field;
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
	
	/**
	 * uses reflection to convert java object to json string
	 * @param object object to stringify
	 * @return JSON.stringified object
	 */
	public static <T extends Object> String toJSONObject(T object) {
		String result = "{";
        for (Field field : object.getClass().getFields()) {
            try {
                Object o = field.get(object);
                String value = field.getType().isPrimitive() ? "%s" : "\"%s\"";
                result += String.format("\"%s\":%s,", field.getName(), String.format(value, o.toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (object.getClass().getFields().length > 0)
        	result = result.substring(0, result.length() - 1);
		result += "}";
		System.out.println(result);
    	return result;
	}

	/**
	 * uses reflection to convert java collection of objects to json string
	 * @param array collection to stringify
	 * @return JSON.stringified array
	 */
	public static <T extends Object> String toJSONArray(Collection<T> array) {
		String result = "[";
		for (T object : array)
			result += String.format("%s,", Algorithms.toJSONObject(object));
		if (array.size() > 0)
			result = result.substring(0, result.length() - 1);
		result += "]";
    	return result;
	}
}
