package com.nathangawith.umkc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nathangawith.umkc.Config;
import com.nathangawith.umkc.Messages;
import com.nathangawith.umkc.dtos.DBUser;
import com.nathangawith.umkc.repositories.IAuthenticationRepository;

@Component("account_service")
public class AuthenticationService implements IAuthenticationService {

    private IAuthenticationRepository mAccountRepository;

    @Autowired
    public AuthenticationService(
        @Qualifier("account_repository")
        IAuthenticationRepository aAccountRepository
    ) {
        this.mAccountRepository = aAccountRepository;
    }

	@Override
	public String getToken(String username, String password) {
		DBUser user = mAccountRepository.isValidLogin(username, password);
		if (user != null) {
			int issued_at = (int) (System.currentTimeMillis() / 1000);
			int expire_at = issued_at + (60 * Config.tokenLifespan);
		    Algorithm algorithm = Algorithm.HMAC256(Config.jwtSecretKey);
		    return JWT.create()
		        .withIssuer("auth0")
		        .withClaim("preferred_username", user.Username)
		        .withClaim("user_id", user.UserID)
		        .withClaim("iat", issued_at)
		        .withClaim("exp", expire_at)
		        .sign(algorithm);
		} else {
			return null;
		}
	}

	@Override
	public boolean createUser(String username, String password) throws Exception {
		if (mAccountRepository.doesUsernameExist(username))
			throw new Exception(Messages.USERNAME_ALREADY_EXISTS);
		else
			return mAccountRepository.insertNewUser(username, password);
	}

}
