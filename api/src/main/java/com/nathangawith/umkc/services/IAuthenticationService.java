package com.nathangawith.umkc.services;

public interface IAuthenticationService {
	public String  getToken  (String username, String password);
	public boolean createUser(String username, String password) throws Exception;
}
