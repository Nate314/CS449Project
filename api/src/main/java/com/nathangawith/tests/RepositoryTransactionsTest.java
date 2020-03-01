package com.nathangawith.tests;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.Queries;
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.DBTransaction;
import com.nathangawith.umkc.repositories.TransactionsRepository;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryTransactionsTest {

	@Mock
	private IDatabase mDatabase;

	@InjectMocks
	private TransactionsRepository mRepository = new TransactionsRepository();

	@Test
	public void insertTransactionTest() {
		insertTransactionGenericTest(true);
		insertTransactionGenericTest(false);
	}
	
	private void insertTransactionGenericTest(boolean success) {
		// Arrange
		DBTransaction transaction = new DBTransaction();
		transaction.UserID = 1;
		transaction.AccountID = 1;
		transaction.CategoryID = 1;
		transaction.Description = "transaction_description";
		transaction.Amount = 3.14;
		transaction.Date = new Date();
		String sql = Queries.INSERT_TRANSACTION;
		List<String> params = Arrays.asList(new String[] { transaction.UserID + "", transaction.AccountID + "", transaction.CategoryID + "", transaction.Description, transaction.Amount + "", Algorithms.dateToString(transaction.Date) });
		Mockito.when(mDatabase.execute(sql, params)).thenReturn(success);
		
		// Act
		boolean result = mRepository.insertTransaction(transaction.UserID, transaction.AccountID, transaction.CategoryID, transaction.Description, transaction.Amount, transaction.Date);
		
		// Assert
		Assert.assertEquals(success, result);
	}
}
