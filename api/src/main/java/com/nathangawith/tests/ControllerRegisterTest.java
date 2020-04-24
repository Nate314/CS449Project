package com.nathangawith.tests;

import java.util.ArrayList;
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
import com.nathangawith.umkc.controllers.RegisterController;
import com.nathangawith.umkc.dtos.GenericResponse;
import com.nathangawith.umkc.dtos.Transaction;
import com.nathangawith.umkc.services.RegisterService;

@RunWith(MockitoJUnitRunner.class)
public class ControllerRegisterTest extends BaseTest {

	@Mock
	private RegisterService mService;
	
	@InjectMocks
	private RegisterController mController = new RegisterController();
	
	@Test
	public void getTotalTest() {
		getTotalGenericTest(false);
		getTotalGenericTest(true);
	}
	
	@Test
	public void getTransactionsTest() {
		getTransactionsGenericTest(false);
		getTransactionsGenericTest(true);
	}
	
	private void getTotalGenericTest(boolean throwException) {
		// Arrange
		String total = "$3.14";
		GenericResponse genericResponse = new GenericResponse();
		try {
			if (throwException)
				Mockito.when(mService.getTotal(1)).thenThrow(new Exception("some_error"));
			else
				Mockito.when(mService.getTotal(1)).thenReturn(total);
			
			// Act
			ResponseEntity<String> response = mController.getTotal(request);
			
			// Assert
			if (throwException) {
				Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
				genericResponse.response = "some_error";
				Assert.assertEquals(Algorithms.toJSONObject(genericResponse), response.getBody());
			} else {
				Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
				genericResponse.response = total;
				Assert.assertEquals(Algorithms.toJSONObject(genericResponse), response.getBody());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	private void getTransactionsGenericTest(boolean throwException) {
		// Arrange
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		Transaction tranasction = new Transaction();
		tranasction.AccountDescription = "test account";
		tranasction.Amount = 3.14;
		tranasction.Date = new Date();
		tranasction.Description = "some income desc";
		tranasction.CategoryDescription = "some income cat";
		transactions.add(tranasction);
		GenericResponse genericResponse = new GenericResponse();
		try {
			if (throwException)
				Mockito.when(mService.getTransactions(1)).thenThrow(new Exception("some_error"));
			else
				Mockito.when(mService.getTransactions(1)).thenReturn(transactions);
			
			// Act
			ResponseEntity<String> response = mController.getTransactions(request);
			
			// Assert
			if (throwException) {
				Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
				genericResponse.response = "some_error";
				Assert.assertEquals(Algorithms.toJSONObject(genericResponse), response.getBody());
			} else {
				Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
				Assert.assertEquals(Algorithms.toJSONArray(transactions), response.getBody());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
