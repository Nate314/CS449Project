package com.nathangawith.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.nathangawith.umkc.dtos.Transaction;
import com.nathangawith.umkc.repositories.IRegisterRepository;
import com.nathangawith.umkc.services.RegisterService;

@RunWith(MockitoJUnitRunner.class)
public class ServiceRegisterTest {

	@Mock
	private IRegisterRepository mRepository;
	
	@InjectMocks
	private RegisterService mService = new RegisterService(mRepository);

	@Test
	public void getTotalTest() {
		getTotalGenericTest(3.14);
		getTotalGenericTest(-3.14);
	}

	@Test
	public void getTransactionsTest() {
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		getTransactionsGenericTest(transactions);
		Transaction tranasction = new Transaction();
		tranasction.AccountDescription = "test account";
		tranasction.Amount = 3.14;
		tranasction.Date = new Date();
		tranasction.Description = "some income desc";
		tranasction.CategoryDescription = "some income cat";
		transactions.add(tranasction);
		getTransactionsGenericTest(transactions);
	}
	
	private void getTotalGenericTest(double expectedResult) {
		// Arrange
		Mockito.when(mRepository.selectTotal(1)).thenReturn(expectedResult);		
		
		// Act
		try {
			String result = mService.getTotal(1);
			
			// Assert
			if (expectedResult < 0)
				Assert.assertEquals("-$" + (-1 * expectedResult), result);
			else
				Assert.assertEquals("$" + expectedResult, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	private void getTransactionsGenericTest(Collection<Transaction> expectedResult) {
		// Arrange
		Mockito.when(mRepository.selectTransactions(1)).thenReturn(expectedResult);		
		
		// Act
		try {
			Collection<Transaction> result = mService.getTransactions(1);
			
			// Assert
			Assert.assertEquals(expectedResult, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}
	}
}
