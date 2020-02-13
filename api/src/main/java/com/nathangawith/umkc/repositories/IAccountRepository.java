package com.nathangawith.umkc.repositories;

public interface IAccountRepository {
	boolean doesUsernameExist(String username);
    boolean isValidLogin(String username, String password);
    boolean insertNewUser(String username, String password);
}
