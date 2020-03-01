package com.nathangawith.tests;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.nathangawith.umkc.dtos.DBTransaction;
import com.nathangawith.umkc.repositories.ITransactionsRepository;
import com.nathangawith.umkc.services.TransactionsService;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTransactionsTest  {

	@Mock
	private ITransactionsRepository mRepository;
	
	@InjectMocks
	private TransactionsService mService = new TransactionsService(mRepository);

	@Test
	public void addTransactionTest() {
		addTransactionGenericTest(true);
		addTransactionGenericTest(false);
	}
	
	private void addTransactionGenericTest(boolean success) {
		// Arrange
		DBTransaction transaction = new DBTransaction();
		transaction.UserID = 1;
		transaction.AccountID = 1;
		transaction.CategoryID = 1;
		transaction.Description = "transaction_description";
		transaction.Amount = 3.14;
		transaction.Date = new Date();
		Mockito.when(mRepository.insertTransaction(transaction.UserID, transaction.AccountID, transaction.CategoryID, transaction.Description, transaction.Amount, transaction.Date)).thenReturn(success);

		// Act
		boolean result = mService.addNewTransaction(transaction.UserID, transaction.AccountID, transaction.CategoryID, transaction.Description, transaction.Amount, transaction.Date);
		
		// Assert
		Assert.assertEquals(success, result);
	}
}
