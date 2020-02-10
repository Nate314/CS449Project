package com.nathangawith.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.nathangawith.umkc.Config;
import com.nathangawith.umkc.repositories.IAccountRepository;
import com.nathangawith.umkc.services.AccountService;

@RunWith(MockitoJUnitRunner.class)
public class ServiceAccountTest {

	@Mock
	private IAccountRepository mRepository;
	
	@InjectMocks
	private AccountService mService = new AccountService(mRepository);

	@Test
	public void getTokenTest() {
		getTokenGenericTest(true);
		getTokenGenericTest(false);
	}

	@Test
	public void createAccountTest() {
		createAccountGenericTest(true);
		createAccountGenericTest(false);
	}
	
	private void getTokenGenericTest(boolean valid) {
		// Arrange
		Config.jwtSecretKey = "some_secret";
		Mockito.when(mRepository.isValidLogin("u", "p")).thenReturn(valid);		
		
		// Act
		String token = mService.getToken("u", "p");
		System.out.println(token);
		
		// Assert
		if (valid) {
			Assert.assertNotNull(token);
			Assert.assertTrue(token.startsWith("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9"));	
		} else {
			Assert.assertNull(token);
		}
	}

	private void createAccountGenericTest(boolean expectedResult) {
		// Arrange
		Mockito.when(mRepository.insertNewUser("u", "p")).thenReturn(expectedResult);		
		
		// Act
		boolean success = mService.createAccount("u", "p");
		
		// Assert
		Assert.assertEquals(expectedResult, success);
	}
}
