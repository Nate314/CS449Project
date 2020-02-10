package com.nathangawith.umkc.repositories;

public interface IAccountRepository {
    boolean isValidLogin(String username, String password);
    boolean insertNewUser(String username, String password);
}
