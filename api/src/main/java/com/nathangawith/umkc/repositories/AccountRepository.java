package com.nathangawith.umkc.repositories;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.DBUser;

@Component("account_repository")
public class AccountRepository implements IAccountRepository {

	@Autowired
	@Qualifier("my_database")
	IDatabase DB;
	
	public AccountRepository(
//		@Qualifier("my_database")
//		IDatabase db
	) {
//		DB = db;
//		DB = new Database();
	}

	@Override
	public boolean isValidLogin(String username, String password) {
		String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
		String hashedPass = this.hashAndSalt(password);
		List<String> params = Arrays.asList(new String[] {username, hashedPass});
		DBUser user = DB.selectFirst(sql, params, DBUser.class);
		return user != null;
//		return true;
	}
	
	@Override
	public boolean insertNewUser(String username, String password) {
		String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
		String hashedPass = this.hashAndSalt(password);
		List<String> params = Arrays.asList(new String[] {username, hashedPass});
		boolean success = DB.execute(sql, params);
		return success;
	}
	
	private String hashAndSalt(String password) {
		System.out.println("Hashing and salting . . . ");
		System.out.println(password);
		String modifiedPassword = "";
		for (char c : password.toCharArray()) modifiedPassword += c + "n";
		System.out.println(modifiedPassword);
		String result = "";
		for (char c : modifiedPassword.toCharArray()) result += c + 15;
		System.out.println(result);
		System.out.println(" . . . Hashed and salted");
		return result;
	}

}
