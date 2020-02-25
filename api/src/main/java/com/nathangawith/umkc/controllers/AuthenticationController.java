package com.nathangawith.umkc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nathangawith.umkc.Messages;
import com.nathangawith.umkc.dtos.LoginRequest;
import com.nathangawith.umkc.services.IAuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    @Qualifier("account_service")
    private IAuthenticationService authenticationService;

    @RequestMapping(value = "/login",
    		method = RequestMethod.POST
    )
    public ResponseEntity<String> postLogin(
    		@RequestBody LoginRequest credentials
    	) throws Exception {
    	String token = authenticationService.getToken(credentials.username, credentials.password);
    	String response = String.format("{\"token\":\"%s\"}", token);
	    return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/createaccount",
    		method = RequestMethod.POST
    )
    public ResponseEntity<String> postCreateLogin(
    		@RequestBody LoginRequest credentials
    	) throws Exception {
    	try {
        	if (authenticationService.createUser(credentials.username, credentials.password)) {
        	    return new ResponseEntity<String>(Messages.USER_CREATED_SUCCESSFULLY, HttpStatus.OK);
        	} else {
        		throw new Exception(Messages.USER_CREATION_FAILED);
        	}
    	} catch (Exception ex) {
    		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    	}
    }
}
