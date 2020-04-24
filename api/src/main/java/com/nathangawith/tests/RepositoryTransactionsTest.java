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
import com.nathangawith.umkc.dtos.TransactionRequest;
import com.nathangawith.umkc.repositories.TransactionsRepository;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryTransactionsTest extends BaseTest {

	@Mock
	private IDatabase mDatabase;

	@InjectMocks
	private TransactionsRepository mRepository = new TransactionsRepository();

	@Test
	public void insertTransactionTest() {
		insertTransactionGenericTest(true);
		insertTransactionGenericTest(false);
	}

	@Test
	public void insertTransferTest() {
		for (boolean i : trueandfalse) {
			for (boolean j : trueandfalse) {
				for (boolean k : trueandfalse) {
					insertTransferGenericTest(i, j, k);
				}
			}
		}
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
	
	private void insertTransferGenericTest(boolean isAccountTransfer, boolean twoTransactionIDsReturned, boolean success) {
		// Arrange
		TransactionRequest transaction = new TransactionRequest();
		transaction.UserID = 1;
		transaction.AccountID = 1;
		transaction.CategoryID = 1;
		transaction.Description = "transaction_description";
		transaction.Amount = 3.14;
		transaction.AccountFromID = 52;
		transaction.AccountToID = 53;
		transaction.CategoryFromID = 54;
		transaction.CategoryToID = 55;
		transaction.Date = new Date();
		int fromAccountID = -1, toAccountID = -1, fromCategoryID = -1, toCategoryID = -1;
		if (isAccountTransfer) {
			fromAccountID = transaction.AccountFromID;
			toAccountID = transaction.AccountToID;
		} else {
			fromCategoryID = transaction.CategoryFromID;
			toCategoryID = transaction.CategoryToID;
		}
		String sql;
		List<String> params;
		sql = Queries.INSERT_TRANSACTION;
		params = Algorithms.params(transaction.UserID, fromAccountID, fromCategoryID, transaction.Description, -1 * transaction.Amount, Algorithms.dateToString(transaction.Date));
		Mockito.when(mDatabase.execute(sql, params)).thenReturn(success);
		params = Algorithms.params(transaction.UserID, toAccountID, toCategoryID, transaction.Description, transaction.Amount, Algorithms.dateToString(transaction.Date));
		Mockito.when(mDatabase.execute(sql, params)).thenReturn(success);
		params = Algorithms.params(transaction.UserID);
		DBTransaction qrTransaction1 = new DBTransaction();
		DBTransaction qrTransaction2 = new DBTransaction();
		qrTransaction1.TransactionID = 12;
		qrTransaction2.TransactionID = 13;
		sql = Queries.GET_FROM_TO_TRANSACTION_IDS;
		List<DBTransaction> queryResult = Arrays.asList(twoTransactionIDsReturned ? new DBTransaction[] {qrTransaction1, qrTransaction2} : new DBTransaction[] {qrTransaction1});
		Mockito.when(mDatabase.select(sql, params, DBTransaction.class)).thenReturn(queryResult);
		if (twoTransactionIDsReturned) {
			sql = Queries.INSERT_TRANSFER;
			params = Algorithms.params(transaction.UserID, qrTransaction2.TransactionID, qrTransaction1.TransactionID);
			Mockito.when(mDatabase.execute(sql, params)).thenReturn(success);
		}
		boolean expectedResult = twoTransactionIDsReturned ? success : false;

		if (expectedResult) {
			System.out.println("true");
		}
		
		// Act
		int fromID = isAccountTransfer ? fromAccountID : fromCategoryID;
		int toID = isAccountTransfer ? toAccountID : toCategoryID;
		boolean result = mRepository.insertTransfer(transaction.UserID, fromID, toID, isAccountTransfer, transaction.Description, transaction.Amount, transaction.Date);
		
		// Assert
		Assert.assertEquals(expectedResult, result);
	}
}
