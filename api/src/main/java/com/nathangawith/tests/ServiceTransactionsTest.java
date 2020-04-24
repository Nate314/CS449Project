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
public class ServiceTransactionsTest extends BaseTest  {

	@Mock
	private ITransactionsRepository mRepository;
	
	@InjectMocks
	private TransactionsService mService = new TransactionsService(mRepository);

	@Test
	public void addTransactionTest() {
		addTransactionTransferGenericTest(false, true);
		addTransactionTransferGenericTest(false, false);
	}

	@Test
	public void addTransferTest() {
		addTransactionTransferGenericTest(true, true);
		addTransactionTransferGenericTest(true, false);
	}
	
	private void addTransactionTransferGenericTest(boolean transfer, boolean success) {
		// Arrange
		DBTransaction transaction = new DBTransaction();
		transaction.UserID = 1;
		transaction.AccountID = 1;
		transaction.CategoryID = 1;
		transaction.Description = "transaction_description";
		transaction.Amount = 3.14;
		transaction.Date = new Date();
		if (transfer) {
			Mockito.when(mRepository.insertTransfer(transaction.UserID, transaction.AccountID, transaction.CategoryID, true, transaction.Description, transaction.Amount, transaction.Date)).thenReturn(success);
		} else {
			Mockito.when(mRepository.insertTransaction(transaction.UserID, transaction.AccountID, transaction.CategoryID, transaction.Description, transaction.Amount, transaction.Date)).thenReturn(success);
		}

		// Act
		boolean result;
		if (transfer) {
			result = mService.addNewTransfer(transaction.UserID, transaction.AccountID, transaction.CategoryID, true, transaction.Description, transaction.Amount, transaction.Date);
		} else {
			result = mService.addNewTransaction(transaction.UserID, transaction.AccountID, transaction.CategoryID, transaction.Description, transaction.Amount, transaction.Date);
		}
		
		// Assert
		Assert.assertEquals(success, result);
	}
}
