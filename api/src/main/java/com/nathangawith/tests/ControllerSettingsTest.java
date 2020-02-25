package com.nathangawith.tests;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.nathangawith.umkc.Messages;
import com.nathangawith.umkc.controllers.SettingsController;
import com.nathangawith.umkc.dtos.DBAccount;
import com.nathangawith.umkc.services.SettingsService;

@RunWith(MockitoJUnitRunner.class)
public class ControllerSettingsTest {

	@Mock
	private SettingsService mService;
	
	@InjectMocks
	private SettingsController mController = new SettingsController();
	
	@Test
	public void postLoginTest() {
		postLoginGenericTest(false, false);
		postLoginGenericTest(false, true);
		postLoginGenericTest(true, true);
	}

	private void postLoginGenericTest(boolean accountExists, boolean creationFailed) {
		try {
			// Arrange
			HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
			Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJpc3MiOiJhdXRoMCIsInByZWZlcnJlZF91c2VybmFtZSI6InUiLCJleHAiOjE1ODI2MDU0MDIsImlhdCI6MTU4MjYwNDUwMn0.AVSbXFTQrUYII67oVfDhMJ3SzA22RgQXcDqlfIyKb00");
			DBAccount body = new DBAccount();
			body.AccountID = 1;
			body.Description = "account_description";
			
			if (accountExists) {
				Mockito.when(mService.addAccount(1, "account_description")).thenThrow(new Exception(Messages.ACCOUNT_ALREADY_EXISTS));
			} else {
				Mockito.when(mService.addAccount(1, "account_description")).thenReturn(!creationFailed);
			}
			
			// Act
			ResponseEntity<String> response = mController.postAddAccount(request, body);
			
			// Assert
			if (accountExists) {
				Assert.assertEquals(Messages.ACCOUNT_ALREADY_EXISTS, response.getBody());
			} else {
				String msg = creationFailed ? Messages.ACCOUNT_CREATION_FAILED : Messages.ACCOUNT_CREATED_SUCCESSFULLY;
				Assert.assertEquals(msg, response.getBody());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
