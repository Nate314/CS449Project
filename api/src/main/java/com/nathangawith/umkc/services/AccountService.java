package com.nathangawith.umkc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nathangawith.umkc.Config;
import com.nathangawith.umkc.repositories.IAccountRepository;

@Component("account_service")
public class AccountService implements IAccountService {

    private IAccountRepository mAccountRepository;

    @Autowired
    public AccountService(
        @Qualifier("account_repository")
        IAccountRepository aAccountRepository
    ) {
        this.mAccountRepository = aAccountRepository;
    }

	@Override
	public String getToken(String username, String password) {
		if (mAccountRepository.isValidLogin(username, password)) {
			int issued_at = (int) (System.currentTimeMillis() / 1000);
			int expire_at = issued_at + (1000 * 60 * 15);
		    Algorithm algorithm = Algorithm.HMAC256(Config.jwtSecretKey);
		    return JWT.create()
		        .withIssuer("auth0")
		        .withClaim("preferred_username", username)
		        .withClaim("iat", issued_at)
		        .withClaim("exp", expire_at)
		        .sign(algorithm);
		} else {
			return null;
		}
	}
	
	public boolean createAccount(String username, String password) {
		return mAccountRepository.insertNewUser(username, password);
	}

}
