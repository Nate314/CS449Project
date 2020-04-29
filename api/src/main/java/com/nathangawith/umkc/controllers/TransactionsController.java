package com.nathangawith.umkc.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.nathangawith.umkc.dtos.GenericResponse;
import com.nathangawith.umkc.dtos.TransactionRequest;
import com.nathangawith.umkc.interceptors.JWTInterceptor;
import com.nathangawith.umkc.services.ITransactionsService;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

	@Autowired
	@Qualifier("transactions_service")
	private ITransactionsService transactionsService;
	
	private ResponseEntity<String> okGenericResponse(String message) {
		GenericResponse response = new GenericResponse();
		response.response = message;
		return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.OK);
	}
	
	private ResponseEntity<String> failGenericResponse(String message) {
		GenericResponse response = new GenericResponse();
		response.response = message;
		return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
	}
	
	private ResponseEntity<String> addOrEditOrDelete(HttpServletRequest request, String transactionType, TransactionRequest body, boolean edit, boolean delete) {
		try {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("add_ok", Messages.TRANSACTION_CREATED_SUCCESSFULLY);
			messages.put("add_fail", Messages.TRANSACTION_CREATION_FAILED);
			messages.put("edit_ok", Messages.TRANSACTION_CREATED_SUCCESSFULLY);
			messages.put("edit_fail", Messages.TRANSACTION_CREATION_FAILED);
			messages.put("remove_ok", Messages.TRANSACTION_CREATED_SUCCESSFULLY);
			messages.put("remove_fail", Messages.TRANSACTION_CREATION_FAILED);
			String okMessage = messages.get(String.format("%s_%s", edit ? "edit" : (delete ? "remove" : "add"), "ok"));
			String failMessage = messages.get(String.format("%s_%s", edit ? "edit" : (delete ? "remove" : "add"), "fail"));
			int u = JWTInterceptor.getUserIDFromHeader(request);
			int acc = body.AccountID, cat = body.CategoryID;
			String desc = body.Description;
			double amt = body.Amount;
			Date d = body.Date;
			transactionType = transactionType.toUpperCase();
			if (amt < 1000000 || delete) {
				int transactionID = body.TransactionID;
				if (Arrays.asList(new String[] { Constants.EXPENSE, Constants.INCOME }).contains(transactionType)) {
					amt = (transactionType.equals(Constants.INCOME) ? 1 : -1) * amt;
					boolean success = edit ? transactionsService.editTransaction(u, acc, cat, desc, amt, d, transactionID)
						: (delete ? transactionsService.deleteTransaction(u, transactionID)
							: transactionsService.addNewTransaction(u, acc, cat, desc, amt, d));
					if (success) return this.okGenericResponse(okMessage);
					return this.failGenericResponse(failMessage);
				} else if(Arrays.asList(new String[] { Constants.TRANSFER_ACCOUNT, Constants.TRANSFER_CATEGORY }).contains(transactionType)) {
					boolean isAccountTransfer = transactionType.equals(Constants.TRANSFER_ACCOUNT);
					int fromID = isAccountTransfer ? body.AccountFromID : body.CategoryFromID;
					int toID = isAccountTransfer ? body.AccountToID : body.CategoryToID;
					boolean success = edit ? transactionsService.editTransfer(u, fromID, toID, isAccountTransfer, desc, amt, d, transactionID)
						: (delete ? transactionsService.deleteTransfer(u, transactionID)
							: transactionsService.addNewTransfer(u, fromID, toID, isAccountTransfer, desc, amt, d));
					if (success) return this.okGenericResponse(okMessage);
					return this.failGenericResponse(failMessage);
				}
				return this.failGenericResponse(Messages.INVALID_REQUEST_PARAMETER);
			}
			return this.failGenericResponse(Messages.NO_TRANSACTIONS_OVER_ONE_MILLION);
		} catch (Exception ex) {
			return this.failGenericResponse(ex.getMessage());
		}
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<String> postAddTransaction(
		HttpServletRequest request,
		@RequestParam String transactionType,
		@RequestBody TransactionRequest body
	) throws Exception {
		return addOrEditOrDelete(request, transactionType, body, false, false);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public ResponseEntity<String> putEditTransaction(
		HttpServletRequest request,
		@RequestParam String transactionType,
		@RequestBody TransactionRequest body
	) throws Exception {
		return addOrEditOrDelete(request, transactionType, body, true, false);
	}

	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteRemoveTransaction(
		HttpServletRequest request,
		@RequestParam String transactionType,
		@RequestParam int transactionID
	) throws Exception {
		TransactionRequest transaction = new TransactionRequest();
		transaction.TransactionID = transactionID;
		return addOrEditOrDelete(request, transactionType, transaction, false, true);
	}
}
