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
import com.nathangawith.umkc.controllers.AuthenticationController;
import com.nathangawith.umkc.dtos.LoginRequest;
import com.nathangawith.umkc.services.AuthenticationService;

@RunWith(MockitoJUnitRunner.class)
public class ControllerAuthenticationTest extends BaseTest {

	@Mock
	private AuthenticationService mService;
	
	@InjectMocks
	private AuthenticationController mController = new AuthenticationController();

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
			Assert.assertEquals("{\"token\":\"token\"}", response.getBody());
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
			Mockito.when(mService.createUser("u", "p")).thenReturn(true);

			// Act
			ResponseEntity<String> response = mController.postCreateLogin(credentials);
			
			// Assert
			Assert.assertEquals(Messages.USER_CREATED_SUCCESSFULLY, response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
