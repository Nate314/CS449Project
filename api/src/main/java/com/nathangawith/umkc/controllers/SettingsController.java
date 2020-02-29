package com.nathangawith.umkc.controllers;

import java.util.Arrays;
import java.util.Collection;

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

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.Constants;
import com.nathangawith.umkc.Messages;
import com.nathangawith.umkc.dtos.DBAccount;
import com.nathangawith.umkc.dtos.DBCategory;
import com.nathangawith.umkc.dtos.GenericResponse;
import com.nathangawith.umkc.interceptors.JWTInterceptor;
import com.nathangawith.umkc.services.ISettingsService;

@RestController
@RequestMapping("/settings")
public class SettingsController {

	@Autowired
	@Qualifier("settings_service")
	private ISettingsService settingsService;

	@RequestMapping(value = "/account/add", method = RequestMethod.POST)
	public ResponseEntity<String> postAddAccount(HttpServletRequest request, @RequestBody DBAccount body)
			throws Exception {
		GenericResponse response = new GenericResponse();
		try {
			int userID = JWTInterceptor.getUserIDFromHeader(request);
			String description = body.Description;
			if (settingsService.addAccount(userID, description)) {
				response.response = Messages.ACCOUNT_CREATED_SUCCESSFULLY;
				return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.OK);
			} else {
				response.response = Messages.ACCOUNT_CREATION_FAILED;
				return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
			}
		} catch (Exception ex) {
			response.response = ex.getMessage();
			return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/accounts/all", method = RequestMethod.GET)
	public ResponseEntity<String> getAllAccounts(HttpServletRequest request) throws Exception {
		try {
			int userID = JWTInterceptor.getUserIDFromHeader(request);
			Collection<DBAccount> accounts = settingsService.getAccounts(userID);
			return new ResponseEntity<String>(Algorithms.toJSONArray(accounts), HttpStatus.OK);
		} catch (Exception ex) {
			GenericResponse response = new GenericResponse();
			response.response = ex.getMessage();
			return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/category/add", method = RequestMethod.POST)
	public ResponseEntity<String> postAddCategory(HttpServletRequest request, @RequestParam String categoryType,
			@RequestBody DBCategory body) throws Exception {
		GenericResponse response = new GenericResponse();
		try {
			int userID = JWTInterceptor.getUserIDFromHeader(request);
			String description = body.Description;
			categoryType = categoryType.toUpperCase();
			if (Arrays.asList(new String[] { Constants.EXPENSE, Constants.INCOME }).contains(categoryType)) {
				categoryType = categoryType.equals(Constants.INCOME) ? Constants.INCOME : Constants.EXPENSE;
				if (settingsService.addCategory(userID, categoryType, description)) {
					response.response = Messages.CATEGORY_CREATED_SUCCESSFULLY;
					return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.OK);
				} else {
					response.response = Messages.CATEGORY_CREATION_FAILED;
					return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
				}
			} else {
				response.response = Messages.INVALID_REQUEST_PARAMETER;
				return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
			}
		} catch (Exception ex) {
			response.response = ex.getMessage();
			return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/categories/all", method = RequestMethod.GET)
	public ResponseEntity<String> getAllCategories(HttpServletRequest request, @RequestParam String categoryType)
			throws Exception {
		GenericResponse response = new GenericResponse();
		try {
			int userID = JWTInterceptor.getUserIDFromHeader(request);
			categoryType = categoryType.toUpperCase();
			if (Arrays.asList(new String[] { Constants.EXPENSE, Constants.INCOME }).contains(categoryType)) {
				categoryType = categoryType.equals(Constants.INCOME) ? Constants.INCOME : Constants.EXPENSE;
				Collection<DBCategory> categories = settingsService.getCategories(userID, categoryType);
				return new ResponseEntity<String>(Algorithms.toJSONArray(categories), HttpStatus.OK);
			} else {
				response.response = Messages.INVALID_REQUEST_PARAMETER;
				return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
			}
		} catch (Exception ex) {
			response.response = ex.getMessage();
			return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
		}
	}
}
