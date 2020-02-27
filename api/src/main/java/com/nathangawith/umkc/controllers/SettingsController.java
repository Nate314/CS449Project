package com.nathangawith.umkc.controllers;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nathangawith.umkc.Constants;
import com.nathangawith.umkc.Messages;
import com.nathangawith.umkc.dtos.DBAccount;
import com.nathangawith.umkc.dtos.DBCategory;
import com.nathangawith.umkc.interceptors.JWTInterceptor;
import com.nathangawith.umkc.services.ISettingsService;

@RestController
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    @Qualifier("settings_service")
    private ISettingsService settingsService;
    
    private String toJSON(String response) {
    	return String.format("{\"response\":\"%s\"}", response);
    }

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
        	    return new ResponseEntity<String>(toJSON(Messages.ACCOUNT_CREATED_SUCCESSFULLY), HttpStatus.OK);
        	} else {
        		return new ResponseEntity<String>(toJSON(Messages.ACCOUNT_CREATION_FAILED), HttpStatus.NOT_FOUND);
        	}
    	} catch (Exception ex) {
    		return new ResponseEntity<String>(toJSON(ex.getMessage()), HttpStatus.NOT_FOUND);
    	}
    }

    @RequestMapping(value = "/category/add",
		method = RequestMethod.POST
    )
    public ResponseEntity<String> postAddCategory(
    		HttpServletRequest request,
    		@RequestParam String categoryType,
    		@RequestBody DBCategory body
    	) throws Exception {
    	try {
        	int userID = JWTInterceptor.getUserIDFromHeader(request);
        	String description = body.Description;
        	categoryType = categoryType.toUpperCase();
        	if (Arrays.asList(new String[] { Constants.EXPENSE, Constants.INCOME }).contains(categoryType)) {
        		categoryType = categoryType.equals(Constants.INCOME) ? Constants.INCOME : Constants.EXPENSE; 
            	if (settingsService.addCategory(userID, categoryType, description)) {
            	    return new ResponseEntity<String>(toJSON(Messages.CATEGORY_CREATED_SUCCESSFULLY), HttpStatus.OK);
            	} else {
            		return new ResponseEntity<String>(toJSON(Messages.CATEGORY_CREATION_FAILED), HttpStatus.NOT_FOUND);
            	}
        	} else {
        		return new ResponseEntity<String>(toJSON(Messages.INVALID_REQUEST_PARAMETER), HttpStatus.NOT_FOUND);
        	}
    	} catch (Exception ex) {
    		return new ResponseEntity<String>(toJSON(ex.getMessage()), HttpStatus.NOT_FOUND);
    	}
    }
}
