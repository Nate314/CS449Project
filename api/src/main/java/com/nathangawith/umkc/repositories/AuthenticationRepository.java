package com.nathangawith.umkc.repositories;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.Queries;
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.DBUser;

@Component("account_repository")
public class AuthenticationRepository implements IAuthenticationRepository {

	@Autowired
	@Qualifier("my_database")
	IDatabase DB;

	@Override
	public boolean doesUsernameExist(String username) {
		List<String> params = Arrays.asList(new String[] { username });
		DBUser user = DB.selectFirst(Queries.GET_USER, params, DBUser.class);
		return user != null;
	}

	@Override
	public DBUser isValidLogin(String username, String password) {
		String hashedPass = Algorithms.encryptPassword(username, password);
		List<String> params = Arrays.asList(new String[] { username, hashedPass });
		DBUser user = DB.selectFirst(Queries.GET_USER_PASSWORD, params, DBUser.class);
		return user;
	}

	@Override
	public boolean insertNewUser(String username, String password) {
		String hashedPass = Algorithms.encryptPassword(username, password);
		List<String> params = Arrays.asList(new String[] { username, hashedPass });
		boolean success = DB.execute(Queries.INSERT_USER, params);
		return success;
	}

}
