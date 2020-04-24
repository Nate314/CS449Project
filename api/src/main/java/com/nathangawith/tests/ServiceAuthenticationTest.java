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
import com.nathangawith.umkc.dtos.DBUser;
import com.nathangawith.umkc.repositories.IAuthenticationRepository;
import com.nathangawith.umkc.services.AuthenticationService;

@RunWith(MockitoJUnitRunner.class)
public class ServiceAuthenticationTest extends BaseTest {

	@Mock
	private IAuthenticationRepository mRepository;
	
	@InjectMocks
	private AuthenticationService mService = new AuthenticationService(mRepository);

	@Test
	public void getTokenTest() {
		getTokenGenericTest(true);
		getTokenGenericTest(false);
	}

	@Test
	public void createUserTest() {
		createUserGenericTest(true, true, false);
		createUserGenericTest(true, false, false);
		createUserGenericTest(false, true, true);
		createUserGenericTest(false, false, false);
	}
	
	private void getTokenGenericTest(boolean valid) {
		// Arrange
		DBUser user = new DBUser();
		user.UserID = 1;
		user.Username = "u";
		Config.jwtSecretKey = "some_secret";
		Config.tokenLifespan = 15;
		Mockito.when(mRepository.isValidLogin("u", "p")).thenReturn(valid ? user : null);		
		
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

	private void createUserGenericTest(boolean userExists, boolean insertSuccessful, boolean expectedResult) {
		// Arrange
		Mockito.when(mRepository.doesUsernameExist("u")).thenReturn(userExists);
		Mockito.when(mRepository.insertNewUser("u", "p")).thenReturn(insertSuccessful);		
		
		// Act
		try {
			boolean success = mService.createUser("u", "p");
			
			// Assert
			Assert.assertEquals(expectedResult, success);
		} catch (Exception e) {
			if (userExists) {
				Assert.assertEquals(e.getMessage(), Messages.USERNAME_ALREADY_EXISTS);
			} else {
				e.printStackTrace();
				Assert.fail();
			}
		}
	}
}
