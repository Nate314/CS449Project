package com.nathangawith.tests;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.Constants;
import com.nathangawith.umkc.Messages;
import com.nathangawith.umkc.controllers.TransactionsController;
import com.nathangawith.umkc.dtos.GenericResponse;
import com.nathangawith.umkc.dtos.TransactionRequest;
import com.nathangawith.umkc.services.TransactionsService;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTransactionsTest extends BaseTest {

	@Mock
	private TransactionsService mService;
	
	@InjectMocks
	private TransactionsController mController = new TransactionsController();
	
	@Test
	public void postAddTransactionTest() {
		postAddTransactionGenericTest(false);
		postAddTransactionGenericTest(true);
	}

	private void postAddTransactionGenericTest(boolean creationFailed) {
		try {
			// Arrange
			GenericResponse genericResponse = new GenericResponse();
			TransactionRequest body = new TransactionRequest();
			body.UserID = 1;
			body.AccountID = 1;
			body.CategoryID = 1;
			body.Description = "transaction_description";
			body.Amount = 3.14;
			body.Date = new Date();

			Mockito.when(mService.addNewTransaction(body.UserID, body.AccountID, body.CategoryID, body.Description, body.Amount, body.Date)).thenReturn(!creationFailed);
			
			// Act
			ResponseEntity<String> response = mController.postAddTransaction(request, Constants.INCOME, body);
			
			// Assert
			Assert.assertEquals(response.getStatusCode(), creationFailed ? HttpStatus.NOT_FOUND : HttpStatus.OK);
			String msg = creationFailed ? Messages.TRANSACTION_CREATION_FAILED : Messages.TRANSACTION_CREATED_SUCCESSFULLY;
			genericResponse.response = msg;
			Assert.assertEquals(Algorithms.toJSONObject(genericResponse), response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
