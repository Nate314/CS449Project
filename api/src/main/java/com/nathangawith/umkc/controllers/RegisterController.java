package com.nathangawith.umkc.controllers;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.dtos.GenericResponse;
import com.nathangawith.umkc.dtos.Transaction;
import com.nathangawith.umkc.interceptors.JWTInterceptor;
import com.nathangawith.umkc.services.IRegisterService;

@RestController
@RequestMapping("/register")
public class RegisterController {

	@Autowired
	@Qualifier("register_service")
	private IRegisterService registerService;

	@RequestMapping(value = "/transactions", method = RequestMethod.GET)
	public ResponseEntity<String> getTransactions(HttpServletRequest request)
			throws Exception {
		GenericResponse response = new GenericResponse();
		try {
			int userID = JWTInterceptor.getUserIDFromHeader(request);
			Collection<Transaction> resp = registerService.getTransactions(userID);
			return new ResponseEntity<String>(Algorithms.toJSONArray(resp), HttpStatus.OK);
		} catch (Exception ex) {
			response.response = ex.getMessage();
			return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/total", method = RequestMethod.GET)
	public ResponseEntity<String> getTotal(HttpServletRequest request) throws Exception {
		GenericResponse response = new GenericResponse();
		try {
			int userID = JWTInterceptor.getUserIDFromHeader(request);
			response.response = registerService.getTotal(userID);
			return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.OK);
		} catch (Exception ex) {
			response.response = ex.getMessage();
			return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
		}
	}
}
