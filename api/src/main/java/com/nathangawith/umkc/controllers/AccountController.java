package com.nathangawith.umkc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nathangawith.umkc.dtos.LoginRequest;
import com.nathangawith.umkc.services.IAccountService;

@RestController
@RequestMapping("/auth")
public class AccountController {

    @Autowired
    @Qualifier("account_service")
    private IAccountService accountService;
    
//    IAccountService accountService;
//	
//	public AccountController(
//	    @Qualifier("account_service")
//	    IAccountService accountService
//	) {
//		this.accountService = accountService;
//	}

    @RequestMapping(value = "/login",
    		method = RequestMethod.POST
    )
    public ResponseEntity<String> postLogin(
    		@RequestBody LoginRequest credentials
    	) throws Exception {
    	String token = accountService.getToken(credentials.username, credentials.password);
	    return new ResponseEntity<String>(token, HttpStatus.OK);
    }

    @RequestMapping(value = "/createaccount",
    		method = RequestMethod.POST
    )
    public ResponseEntity<Boolean> postCreateLogin(
    		@RequestBody LoginRequest credentials
    	) throws Exception {
    	boolean success = accountService.createAccount(credentials.username, credentials.password);
	    return new ResponseEntity<Boolean>(success, HttpStatus.OK);
    }
}
