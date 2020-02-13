package com.nathangawith.umkc.services;

public interface IAccountService {
    String getToken(String username, String password);
    boolean createAccount(String username, String password) throws Exception;
}
