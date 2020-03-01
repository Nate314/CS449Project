package com.nathangawith.umkc.controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.Messages;
import com.nathangawith.umkc.dtos.DBTransaction;
import com.nathangawith.umkc.dtos.GenericResponse;
import com.nathangawith.umkc.services.ITransactionsService;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

	@Autowired
	@Qualifier("transactions_service")
	private ITransactionsService transactionsService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<String> postAddAccount(
		HttpServletRequest request,
		@RequestBody DBTransaction body
	) throws Exception {
		GenericResponse response = new GenericResponse();
		try {
			int u = body.UserID, acc = body.AccountID, cat = body.CategoryID;
			String desc = body.Description;
			double amt = body.Amount;
			Date d = body.Date;
			if (transactionsService.addNewTransaction(u, acc, cat, desc, amt, d)) {
				response.response = Messages.TRANSACTION_CREATED_SUCCESSFULLY;
				return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.OK);
			} else {
				response.response = Messages.TRANSACTION_CREATION_FAILED;
				return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
			}
		} catch (Exception ex) {
			response.response = ex.getMessage();
			return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
		}
	}
}
