package com.nathangawith.umkc.services;

public interface IAuthenticationService {
    String getToken(String username, String password);
    boolean createUser(String username, String password) throws Exception;
}
