package com.nathangawith.umkc.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nathangawith.umkc.Messages;
import com.nathangawith.umkc.dtos.DBAccount;
import com.nathangawith.umkc.interceptors.JWTInterceptor;
import com.nathangawith.umkc.services.ISettingsService;

@RestController
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    @Qualifier("settings_service")
    private ISettingsService settingsService;

    @RequestMapping(value = "/account/add",
		method = RequestMethod.POST
    )
    public ResponseEntity<String> postAddAccount(
    		HttpServletRequest request,
    		@RequestBody DBAccount body
    	) throws Exception {
    	try {
        	int userID = JWTInterceptor.getUserIDFromHeader(request);
        	String description = body.Description;
        	if (settingsService.addAccount(userID, description)) {
        	    return new ResponseEntity<String>(Messages.ACCOUNT_CREATED_SUCCESSFULLY, HttpStatus.OK);
        	} else {
        		return new ResponseEntity<String>(Messages.ACCOUNT_CREATION_FAILED, HttpStatus.OK);
        	}
    	} catch (Exception ex) {
    		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    	}
    }
}
