package com.nathangawith.umkc.repositories;

import com.nathangawith.umkc.dtos.DBUser;

public interface IAuthenticationRepository {
	public boolean doesUsernameExist(String username);
	public DBUser  isValidLogin     (String username, String password);
	public boolean insertNewUser    (String username, String password);
}
