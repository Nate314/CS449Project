package com.nathangawith.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.nathangawith.umkc.Messages;
import com.nathangawith.umkc.controllers.AccountController;
import com.nathangawith.umkc.dtos.LoginRequest;
import com.nathangawith.umkc.services.AccountService;

@RunWith(MockitoJUnitRunner.class)
public class ControllerAccountTest {

	@Mock
	private AccountService mService;
	
	@InjectMocks
	private AccountController mController = new AccountController();

	@Test
	public void postLoginTest() {
		// Arrange
		LoginRequest credentials = new LoginRequest();
		credentials.username = "u";
		credentials.password = "p";
		
		Mockito.when(mService.getToken("u", "p")).thenReturn("token");
		
		// Act
		try {
			ResponseEntity<String> response = mController.postLogin(credentials);
			
			// Assert
			Assert.assertEquals("token", response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void postCreateAccountTest() {
		// Arrange
		LoginRequest credentials = new LoginRequest();
		credentials.username = "u";
		credentials.password = "p";
		
		try {
			Mockito.when(mService.createAccount("u", "p")).thenReturn(true);

			// Act
			ResponseEntity<String> response = mController.postCreateLogin(credentials);
			
			// Assert
			Assert.assertEquals(Messages.ACCOUNT_CREATED_SUCCESSFULLY, response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
