package com.nathangawith.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.nathangawith.umkc.Queries;
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.DBResponse;
import com.nathangawith.umkc.dtos.Transaction;
import com.nathangawith.umkc.repositories.RegisterRepository;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryRegisterTest extends BaseTest {

	@Mock
	private IDatabase mDatabase;

	@InjectMocks
	private RegisterRepository mRepository = new RegisterRepository();

	@Test
	public void selectTotalTest() {
		selectTotalGenericTest(0);
		selectTotalGenericTest(3.14);
	}

	@Test
	public void selectTransactionsTest() {
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		selectTransactionsGenericTest(transactions);
		Transaction tranasction = new Transaction();
		tranasction.AccountDescription = "test account";
		tranasction.Amount = 3.14;
		tranasction.Date = new Date();
		tranasction.Description = "some income desc";
		tranasction.CategoryDescription = "some income cat";
		transactions.add(tranasction);
		selectTransactionsGenericTest(transactions);
	}
	
	private void selectTotalGenericTest(double expectedResult) {
		// Arrange
		String sql = Queries.GET_REGISTER_TOTAL;
		List<String> params = Arrays.asList(new String[] {"1"});
		DBResponse response = new DBResponse();
		response.DOUBLE_RESPONSE = expectedResult;
		Mockito.when(mDatabase.selectFirst(sql, params, DBResponse.class)).thenReturn(response);

		// Act
		double total = mRepository.selectTotal(1);
		
		// Assert
		Assert.assertEquals(expectedResult, total, 0.01);
	}
	
	private void selectTransactionsGenericTest(Collection<Transaction> expectedResult) {
		// Arrange
		String sql = Queries.GET_TRANSACTIONS;
		List<String> params = Arrays.asList(new String[] {"1", "1"});
		Mockito.when(mDatabase.select(sql, params, Transaction.class)).thenReturn(expectedResult);

		// Act
		Collection<Transaction> transactions = mRepository.selectTransactions(1);
		
		// Assert
		Assert.assertEquals(expectedResult, transactions);
	}

}
