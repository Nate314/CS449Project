package com.nathangawith.umkc.repositories;

import com.nathangawith.umkc.dtos.DBUser;

public interface IAuthenticationRepository {
	boolean doesUsernameExist(String username);
    DBUser isValidLogin(String username, String password);
    boolean insertNewUser(String username, String password);
}
