package com.nathangawith.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.nathangawith.umkc.Config;
import com.nathangawith.umkc.Messages;
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
		createAccountGenericTest(true, true, false);
		createAccountGenericTest(true, false, false);
		createAccountGenericTest(false, true, true);
		createAccountGenericTest(false, false, false);
	}
	
	private void getTokenGenericTest(boolean valid) {
		// Arrange
		Config.jwtSecretKey = "some_secret";
		Config.tokenLifespan = 15;
		Mockito.when(mRepository.isValidLogin("u", "p")).thenReturn(valid);		
		
		// Act
		String token = mService.getToken("u", "p");
		
		// Assert
		if (valid) {
			Assert.assertNotNull(token);
			Assert.assertTrue(token.startsWith("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9"));	
		} else {
			Assert.assertNull(token);
		}
	}

	private void createAccountGenericTest(boolean userExists, boolean insertSuccessful, boolean expectedResult) {
		// Arrange
		Mockito.when(mRepository.doesUsernameExist("u")).thenReturn(userExists);
		Mockito.when(mRepository.insertNewUser("u", "p")).thenReturn(insertSuccessful);		
		
		// Act
		try {
			boolean success = mService.createAccount("u", "p");
			
			// Assert
			Assert.assertEquals(expectedResult, success);
		} catch (Exception e) {
			if (userExists) {
				Assert.assertEquals(e.getMessage(), Messages.ACCOUNT_USERNAME_ALREADY_EXISTS);
			} else {
				e.printStackTrace();
				Assert.fail();
			}
		}
	}
}
