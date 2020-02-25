package com.nathangawith.tests;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nathangawith.umkc.Constants;
import com.nathangawith.umkc.Messages;
import com.nathangawith.umkc.controllers.SettingsController;
import com.nathangawith.umkc.dtos.DBAccount;
import com.nathangawith.umkc.dtos.DBCategory;
import com.nathangawith.umkc.services.SettingsService;

@RunWith(MockitoJUnitRunner.class)
public class ControllerSettingsTest {

	@Mock
	private SettingsService mService;
	
	@InjectMocks
	private SettingsController mController = new SettingsController();
	
	@Test
	public void postAddAccountTest() {
		postAddAccountGenericTest(false, false);
		postAddAccountGenericTest(false, true);
		postAddAccountGenericTest(true, true);
	}

	@Test
	public void postAddExpenseCategoryTest() {
		for (String categoryType : new String[] { Constants.INCOME, Constants.EXPENSE } ) {
			postAddCategoryGenericTest(false, categoryType, false);
			postAddCategoryGenericTest(false, categoryType, true);
			postAddCategoryGenericTest(true, categoryType, true);
		}
		postAddCategoryGenericTest(false, "asdf", false);
	}

	private void postAddAccountGenericTest(boolean accountExists, boolean creationFailed) {
		try {
			// Arrange
			HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
			Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJpc3MiOiJhdXRoMCIsInByZWZlcnJlZF91c2VybmFtZSI6InUiLCJleHAiOjE1ODI2MDU0MDIsImlhdCI6MTU4MjYwNDUwMn0.AVSbXFTQrUYII67oVfDhMJ3SzA22RgQXcDqlfIyKb00");
			DBAccount body = new DBAccount();
			body.UserID = 1;
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
				Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
				Assert.assertEquals(Messages.ACCOUNT_ALREADY_EXISTS, response.getBody());
			} else {
				Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
				String msg = creationFailed ? Messages.ACCOUNT_CREATION_FAILED : Messages.ACCOUNT_CREATED_SUCCESSFULLY;
				Assert.assertEquals(msg, response.getBody());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	private void postAddCategoryGenericTest(boolean categoryExists, String categoryType, boolean creationFailed) {
		try {
			// Arrange
			HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
			Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJpc3MiOiJhdXRoMCIsInByZWZlcnJlZF91c2VybmFtZSI6InUiLCJleHAiOjE1ODI2MDU0MDIsImlhdCI6MTU4MjYwNDUwMn0.AVSbXFTQrUYII67oVfDhMJ3SzA22RgQXcDqlfIyKb00");
			DBCategory body = new DBCategory ();
			body.UserID = 1;
			body.Description = "category_description";
			
			if (categoryExists) {
				Mockito.when(mService.addCategory(1, categoryType, "category_description")).thenThrow(new Exception(Messages.CATEGORY_ALREADY_EXISTS));
			} else {
				Mockito.when(mService.addCategory(1, categoryType, "category_description")).thenReturn(!creationFailed);
			}
			
			// Act
			ResponseEntity<String> response = mController.postAddCategory(request, categoryType, body);
			
			// Assert
			if (categoryType.equals(Constants.EXPENSE) || categoryType.equals(Constants.INCOME) ) {
				if (categoryExists) {
					Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
					Assert.assertEquals(Messages.CATEGORY_ALREADY_EXISTS, response.getBody());
				} else {
					Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
					String msg = creationFailed ? Messages.CATEGORY_CREATION_FAILED : Messages.CATEGORY_CREATED_SUCCESSFULLY;
					Assert.assertEquals(msg, response.getBody());
				}
			} else {
				Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
				Assert.assertEquals(Messages.INVALID_REQUEST_PARAMETER, response.getBody());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
